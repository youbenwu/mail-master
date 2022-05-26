package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.constant.NumberConstant;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.PmsProductMapper;
import com.ys.mail.model.admin.query.PmsProductQuery;
import com.ys.mail.model.dto.ProductInfoDTO;
import com.ys.mail.model.vo.PmsProductVO;
import com.ys.mail.service.PmsProductService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public Page<PmsProductVO> getPage(PmsProductQuery query) {
        Page<PmsProductVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return pmsProductMapper.getPage(page, query);
    }

    @Override
    public boolean addOrUpdate(PmsProduct pmsProduct) {
        Long id = pmsProduct.getProductId();
        ApiAssert.noValue(id, CommonResultCode.NULL_USER_ID);

        PmsProduct dbProduct = new PmsProduct();
        String productName = pmsProduct.getProductName();
        boolean existsName;
        if (id.equals(NumberUtils.LONG_ZERO)) {
            // 名称重复检测
            existsName = this.isExistsProductName(productName);
            ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(productName));
            // 拷贝属性
            BeanUtils.copyProperties(pmsProduct, dbProduct);
            // 添加
            dbProduct.setProductId(IdWorker.generateId());
        } else {
            // 修改
            dbProduct = this.getById(id);
            ApiAssert.noValue(dbProduct, CommonResultCode.ID_NO_EXIST);

            // 名称重复检测
            if (!dbProduct.getProductName().equals(productName)) {
                existsName = this.isExistsProductName(productName);
                ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(productName));
            }

            // 拷贝属性
            BeanUtils.copyProperties(pmsProduct, dbProduct);

            // 恢复会员价
            Integer promotionType = pmsProduct.getPromotionType();
            if (BlankUtil.isNotEmpty(promotionType) && !promotionType.equals(NumberConstant.TWO)) {
                dbProduct.setMebPrice(NumberUtils.LONG_ZERO);
                dbProduct.setDisCount(new BigDecimal(1));
            }
        }

        return this.saveOrUpdate(dbProduct);
    }

    @Override
    public ProductInfoDTO getProductInfo(Long productId) {
        return pmsProductMapper.selectProductInfo(productId);
    }

    @Override
    public boolean delete(Long productId) {
        // 商品删除的同时必须连着sku一起删除,否则置换的时候会出现问题
        return pmsProductMapper.delById(productId);
    }

    @Override
    public boolean isExistsProductName(String productName) {
        SqlLambdaQueryWrapper<PmsProduct> wrapper = new SqlLambdaQueryWrapper<>();
        wrapper.eq(PmsProduct::getProductName, productName)
               .last(StringConstant.LIMIT_ONE);
        PmsProduct pmsProduct = this.getOne(wrapper);
        return BlankUtil.isNotEmpty(pmsProduct);
    }
}
