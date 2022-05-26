package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.entity.PmsSkuStock;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.PmsSkuStockMapper;
import com.ys.mail.service.PmsSkuStockService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PmsSkuStockServiceImpl extends ServiceImpl<PmsSkuStockMapper, PmsSkuStock> implements PmsSkuStockService {

    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;

    @Override
    public boolean addOrUpdate(PmsSkuStock pmsSkuStock) {
        Long id = pmsSkuStock.getSkuStockId();
        ApiAssert.noValue(id, CommonResultCode.NULL_USER_ID);

        PmsSkuStock db = new PmsSkuStock();
        String spData = pmsSkuStock.getSpData();
        Long productId = pmsSkuStock.getProductId();
        boolean existsName;
        if (id.equals(NumberUtils.LONG_ZERO)) {
            // 名称重复检测
            existsName = this.isExistsSpData(productId, spData);
            ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(spData));
            // 拷贝属性
            BeanUtils.copyProperties(pmsSkuStock, db);
            // 添加
            db.setSkuStockId(IdWorker.generateId());
        } else {
            // 修改
            db = this.getById(id);
            ApiAssert.noValue(db, CommonResultCode.ID_NO_EXIST);

            // 名称重复检测
            if (!db.getSpData().equals(spData)) {
                existsName = this.isExistsSpData(productId, spData);
                ApiAssert.isTrue(existsName, CommonResultCode.ERR_PARAM_EXIST.getMessage(spData));
            }

            // 拷贝属性
            BeanUtils.copyProperties(pmsSkuStock, db);
        }
        return this.saveOrUpdate(db);
    }

    @Override
    public Page<PmsSkuStock> get(String productName, int pageNum, int pageSize) {
        Page<PmsProduct> page = new Page<>(pageNum, pageSize);
        return pmsSkuStockMapper.get(page, productName);
    }

    @Override
    public boolean isExistsSpData(Long productId, String spData) {
        SqlLambdaQueryWrapper<PmsSkuStock> wrapper = new SqlLambdaQueryWrapper<>();
        wrapper.eq(PmsSkuStock::getProductId, productId)
               .eq(PmsSkuStock::getSpData, spData)
               .last(StringConstant.LIMIT_ONE);
        PmsSkuStock pmsSkuStock = this.getOne(wrapper);
        return BlankUtil.isNotEmpty(pmsSkuStock);
    }
}
