package com.ys.mail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.OmsCartItemParam;
import com.ys.mail.model.vo.OmsCartItemVO;
import com.ys.mail.service.OmsCartItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.service.UmsAddressService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import jodd.util.StringUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ys.mail.mapper.OmsCartItemMapper;
import com.ys.mail.entity.OmsCartItem;

import java.util.List;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-16
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class OmsCartItemServiceImpl extends ServiceImpl<OmsCartItemMapper, OmsCartItem> implements OmsCartItemService {

    @Autowired
    private UmsAddressService addressService;
    @Autowired
    private OmsCartItemMapper cartItemMapper;

    @Override
    public CommonResult<Boolean> batchDelCart(List<Long> ids) {
        boolean b = removeByIds(ids);
        return b ? CommonResult.success("success",true) : CommonResult.failed("error",false);
    }

    @Override
    public CommonResult<Boolean> addCarItem(OmsCartItemParam param) {
        // 首先查出来是否有添加重复的,同一用户,商品，sku，价格记录为同一购物车
        Long userId = UserUtil.getCurrentUser().getUserId();
        QueryWrapper<OmsCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", param.getProductId())
                .eq("price", param.getPic())
                .eq("user_id", userId)
                .eq("product_sku_id", param.getProductSkuId());
        OmsCartItem one = getOne(queryWrapper);
        // 新增和修改
        boolean b ;
        if(BlankUtil.isEmpty(one)){
            OmsCartItem cartItem = new OmsCartItem();
            BeanUtils.copyProperties(param,cartItem);
            cartItem.setCartItemId(IdWorker.generateId());
            cartItem.setProductId(Long.valueOf(param.getProductId()));
            cartItem.setProductSkuId(Long.valueOf(param.getProductSkuId()));
            cartItem.setUserId(userId);
            b = save(cartItem);
        }else{
            b = cartItemMapper.updateQuantity(one.getCartItemId(),param.getQuantity());
        }
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @Override
    public List<OmsCartItem> getCarItem() {
        Long userId=UserUtil.getCurrentUser().getUserId();
        //查询我的购物车
        QueryWrapper<OmsCartItem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).orderByDesc("create_time");
        List<OmsCartItem> cartItemList=list(wrapper);
        return cartItemList;
    }

}
