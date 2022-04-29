package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.mapper.OmsCartItemMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.BatchBuyProductDTO;
import com.ys.mail.service.OmsCartItemService;
import com.ys.mail.service.UmsAddressService;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-16
 */
@Service
public class OmsCartItemServiceImpl extends ServiceImpl<OmsCartItemMapper, OmsCartItem> implements OmsCartItemService {

    //    @Autowired
//    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OmsCartItemMapper cartItemMapper;
    @Autowired
    private UmsAddressService addressService;


//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public CommonResult<Boolean> add(OmsCartItemParam param) {
//        // 添加购物车,不用管是新增还是修改,后台直接group_by{id,id1,id2},{对象},{{id,id1,id2},{对象}}->映射
//        return save(param.getParam(UserUtil.getCurrentUser().getUserId())) ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
//    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<Boolean> add(Long skuId, Integer quantity) {
        OmsCartItem item = cartItemMapper.selectByCartInfo(skuId, quantity);
        ApiAssert.noValue(item, BusinessErrorCode.GOODS_NOT_EXIST);
        item.setCartItemId(IdWorker.generateId());
        item.setUserId(UserUtil.getCurrentUser().getUserId());
        item.setQuantity(quantity);
        return save(item) ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }

//    @Override
//    public List<OmsCartItem> getCarItem() {
//        Long userId=UserUtil.getCurrentUser().getUserId();
//        //查询我的购物车
//        QueryWrapper<OmsCartItem> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_id",userId).orderByDesc("create_time");
//        List<OmsCartItem> cartItemList=list(wrapper);
//        return cartItemList;
//    }

    @Override
    public List<OmsCartItem> list(Long userId) {
        return cartItemMapper.selectList(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeBySkuId(List<Long> ids) {
        return cartItemMapper.removeBySkuId(ids, UserUtil.getCurrentUser().getUserId());
    }

    @Override
    public BatchBuyProductDTO batchProduct(List<Long> ids) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        return new BatchBuyProductDTO(addressService.getByUserId(userId),
                cartItemMapper.batchProduct(ids, userId));

    }

}
