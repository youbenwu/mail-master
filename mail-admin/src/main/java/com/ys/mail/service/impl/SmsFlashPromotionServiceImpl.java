package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.job.CustomTrigger;
import com.ys.mail.mapper.SmsFlashPromotionMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcFlashPromotionParam;
import com.ys.mail.service.SmsFlashPromotionProductService;
import com.ys.mail.service.SmsFlashPromotionService;
import com.ys.mail.util.IdWorker;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

/**
 * <p>
 * 限时购表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SmsFlashPromotionServiceImpl extends ServiceImpl<SmsFlashPromotionMapper, SmsFlashPromotion> implements SmsFlashPromotionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SmsFlashPromotionServiceImpl.class);

    @Autowired
    private SmsFlashPromotionMapper flashPromotionMapper;
    @Autowired
    private SmsFlashPromotionProductService flashPromotionProductService;

    @Autowired
    private CustomTrigger customTrigger;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delFlashPromotion(Long flashPromotionId) throws ParseException {
        QueryWrapper<SmsFlashPromotionProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("flash_promotion_id", flashPromotionId);
        flashPromotionProductService.remove(wrapper);
        boolean b = removeById(flashPromotionId);
        if (b) {
            customTrigger.resetJob();
        }
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createFlashPromotion(PcFlashPromotionParam param) throws ParseException {
        SmsFlashPromotion flashPromotion = new SmsFlashPromotion();
        BeanUtils.copyProperties(param, flashPromotion);
        Long flashPromotionId = flashPromotion.getFlashPromotionId();
        flashPromotion.setFlashPromotionId(flashPromotionId.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : flashPromotionId);
        boolean b = saveOrUpdate(flashPromotion);
        if (b) {
            customTrigger.resetJob();
            // 清除首页缓存
            flashPromotionProductService.delHomeSecondProduct(true);
        }
        return b;
    }


    @Override
    public boolean publicFlashProduct(SmsFlashPromotionProduct smsFlashPromotionProduct) {
        return flashPromotionProductService.saveOrUpdate(smsFlashPromotionProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<Boolean> updateHome(Long flashPromotionId, Boolean homeStatus) {
        boolean b = flashPromotionMapper.updateHome();
        if (b) {
            SmsFlashPromotion flashPromotion = new SmsFlashPromotion(flashPromotionId, homeStatus);
            b = updateById(flashPromotion);
        }
        // 清除首页缓存
        flashPromotionProductService.delHomeSecondProduct(true);
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePublish(Long flashPromotionId, Boolean publishStatus) throws ParseException {
        SmsFlashPromotion flashPromotion = new SmsFlashPromotion(publishStatus, flashPromotionId);

        boolean b = updateById(flashPromotion);
        if (b) {
            customTrigger.resetJob();
        }
        return b;
    }

}
