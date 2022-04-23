package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.OmsCartItemParam;
import com.ys.mail.model.vo.OmsCartItemVO;

import java.util.List;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-16
 */
public interface OmsCartItemService extends IService<OmsCartItem> {
    /**
     * 删除或批量删除购物车商品
     * @param ids 集合id
     * @return 返回值
     */
    CommonResult<Boolean> batchDelCart(List<Long> ids);

    /**
     * 添加商品进购物车
     * @param param 实体参数
     * @return 返回值
     */
    CommonResult<Boolean> addCarItem(OmsCartItemParam param);

    /**
     * 添加商品到购物车
     * @param
     * @return 返回值
     */
    List<OmsCartItem> getCarItem();

}
