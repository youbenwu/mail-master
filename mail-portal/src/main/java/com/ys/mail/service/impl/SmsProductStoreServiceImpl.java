package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.SmsProductStore;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.mapper.SmsProductStoreMapper;
import com.ys.mail.model.param.insert.ProductStoreInsertParam;
import com.ys.mail.model.vo.ProductStoreVO;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.SmsProductStoreService;
import com.ys.mail.service.UserCacheService;
import com.ys.mail.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户_商品_店铺表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-04-25
 */
@Service
public class SmsProductStoreServiceImpl extends ServiceImpl<SmsProductStoreMapper, SmsProductStore> implements SmsProductStoreService {

    @Override
    public ProductStoreVO getInfo() {
        ProductStoreVO vo = new ProductStoreVO();
        SmsProductStore productStore = this.get();
        if (BlankUtil.isNotEmpty(productStore)) {
            BeanUtils.copyProperties(productStore, vo);
            return vo;
        }
        return null;
    }

    @Override
    public SmsProductStore get() {
        Long userId = UserUtil.getCurrentUser().getUserId();
        LambdaQueryWrapper<SmsProductStore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmsProductStore::getUserId, userId)
               .eq(SmsProductStore::getDeleted, false);
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addStore(ProductStoreInsertParam param) {
        UmsUser currentUser = UserUtil.getCurrentUser();
        // 个人店铺数量限制为1
        SmsProductStore info = this.get();
        ApiAssert.haveValue(info, BusinessErrorCode.USER_STORE_EXIST);
        // 店铺名称重复校验
        this.isExistsStoreName(param.getStoreName());
        SmsProductStore productStore = new SmsProductStore();
        BeanUtils.copyProperties(param, productStore);
        // 填充字段
        Long storeId = IdWorker.generateId();
        productStore.setPdtStoreId(storeId);
        productStore.setUserId(currentUser.getUserId());
        return this.save(productStore);
    }

    @Override
    public boolean updateStore(ProductStoreInsertParam param) {
        UmsUser currentUser = UserUtil.getCurrentUser();
        // 查询出个人的店铺信息
        SmsProductStore info = this.get();
        if (BlankUtil.isEmpty(info)) {
            return this.addStore(param);
        }
        // 审核当中则不能修改
        Integer reviewState = info.getReviewState();
        ApiAssert.eq(SmsProductStore.ReviewState.ZERO.key(), reviewState, BusinessErrorCode.USER_STORE_REVIEWING);
        // 根据最后修改时间判断（限制每天修改次数）
        boolean today = DateTool.isToday(info.getUpdateTime());
        ApiAssert.eq(Boolean.TRUE, today, BusinessErrorCode.USER_STORE_REVIEWED);
        // 判断店铺名称不相同则需要校验数据库
        String storeName = param.getStoreName();
        if (!info.getStoreName().equals(storeName)) {
            this.isExistsStoreName(storeName);
        }
        BeanUtils.copyProperties(param, info);
        // 重置审核状态
        info.setReviewState(SmsProductStore.ReviewState.ZERO.key());
        return this.updateById(info);
    }

    @Override
    public void isExistsStoreName(String storeName) {
        LambdaQueryWrapper<SmsProductStore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmsProductStore::getStoreName, storeName);
        List<SmsProductStore> list = this.list(wrapper);
        ApiAssert.haveValue(list, BusinessErrorCode.STORE_NAME_EXIST);
    }

    @Override
    public SmsProductStore getReviewed() {
        SmsProductStore productStore = this.get();
        ApiAssert.noValue(productStore, BusinessErrorCode.USER_STORE_NO_EXIST);

        Integer reviewState = productStore.getReviewState();
        SmsProductStore.ReviewState reviewStateEnum = EnumTool.getEnum(SmsProductStore.ReviewState.class, reviewState);
        switch (reviewStateEnum) {
            case ZERO:
                ApiAssert.fail(BusinessErrorCode.USER_STORE_REVIEWING);
                break;
            case TWO:
                ApiAssert.fail(BusinessErrorCode.USER_STORE_NO_PASS);
                break;
            case ONE:
            default:
        }

        return productStore;
    }

}
