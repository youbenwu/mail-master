package com.ys.mail.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.mapper.SmsFlashPromotionProductMapper;
import com.ys.mail.model.admin.dto.PcFlashPdtDTO;
import com.ys.mail.model.admin.dto.PcFlashPromotionProductDTO;
import com.ys.mail.model.admin.dto.SessionOrPdtDTO;
import com.ys.mail.model.admin.param.PcFlashPromotionProductParam;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.dto.ProductStoreObjDTO;
import com.ys.mail.service.*;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.DateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <p>
 * 商品限时购与商品关系表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SmsFlashPromotionProductServiceImpl extends ServiceImpl<SmsFlashPromotionProductMapper, SmsFlashPromotionProduct> implements SmsFlashPromotionProductService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SmsFlashPromotionProductServiceImpl.class);

    @Autowired
    private SmsFlashPromotionProductMapper flashPromotionProductMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private UmsPartnerService umsPartnerService;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private PmsProductService pmsProductService;
    @Value("${redis.key.homeSecondProduct}")
    private String redisKeyHomeSecondProduct;
    @Autowired
    private SysSettingService sysSettingService;

    @Override
    public boolean create(PcFlashPromotionProductParam param) {
        // 根据商品ID查询合伙人地址信息
        String productId = param.getProductId();
        PmsProduct pmsProduct = pmsProductService.getById(productId);

        // 获取设置中定义的秒杀上架截止时间的最低天数，如：7天
        Integer days = sysSettingService.getSettingValue(SettingTypeEnum.twenty_four);
        Optional.ofNullable(days).map(map -> {
            boolean checkExpireTime = DateTool.checkExpireTime(param.getExpireTime(), days);
            String message = BusinessErrorCode.ERR_DATE_ILLEGAL.getMessage(days);
            ApiAssert.isFalse(checkExpireTime, message);
            return days;
        });

        // 构建参数
        SmsFlashPromotionProduct smsFlashPromotionProduct = param.getParam(param);
        // 添加店铺地址信息
        if (BlankUtil.isNotEmpty(pmsProduct)) {
            UmsPartner umsPartner = umsPartnerService.getById(pmsProduct.getPartnerId());
            if (BlankUtil.isNotEmpty(umsPartner)) {
                // 获取个人店铺信息
                UmsUser umsUser = userManageService.getById(umsPartner.getUserId());
                ProductStoreObjDTO storeObjDTO = new ProductStoreObjDTO();
                storeObjDTO.setStoreName(umsPartner.getCorporateName());
                storeObjDTO.setStorePhone(umsPartner.getPhone());
                storeObjDTO.setStoreBoss(umsUser.getNickname());
                storeObjDTO.setStoreLogo(umsUser.getHeadPortrait());
                String fullAddress = String.format("%s%s%s%s", umsPartner.getProvince(), umsPartner.getCity(), umsPartner.getRegion(), umsPartner.getAddress());
                storeObjDTO.setStoreAddress(fullAddress);
                smsFlashPromotionProduct.setPdtStoreObj(JSON.toJSONString(storeObjDTO));
            }
        }
        return saveOrUpdate(smsFlashPromotionProduct);
    }

    @Override
    public PcFlashPromotionProductDTO getInfoById(Long flashPromotionPdtId) {
        return flashPromotionProductMapper.selectInfoById(flashPromotionPdtId);
    }

    @Override
    public Page<PcFlashPdtDTO> list(Query query) {
        Page<PcFlashPromotionProductDTO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return flashPromotionProductMapper.listAll(page);
    }

    @Override
    public boolean delete(Long flashPromotionPdtId) {
        boolean b = removeById(flashPromotionPdtId);
        delHomeSecondProduct(b);
        return b;
    }

    @Override
    public void replaceFlashPromotionId(Long flashPromotionId, Long replacedFlashPromotionId) {
        flashPromotionProductMapper.replaceFlashPromotionId(flashPromotionId, replacedFlashPromotionId);
    }

    @Override
    public SessionOrPdtDTO getSessionOrPdt() {
        return flashPromotionProductMapper.selectSessionOrPdt();
    }

    @Override
    public void delHomeSecondProduct(boolean b) {
        if (b) {
            LOGGER.info("清除缓存成功:count{}", redisService.keys(redisConfig.fullKey("home:*")));
        }
    }
}
