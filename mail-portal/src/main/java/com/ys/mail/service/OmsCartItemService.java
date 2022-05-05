package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.BatchBuyProductDTO;
import com.ys.mail.model.param.CreateOrderParam;
import com.ys.mail.model.param.OmsCartItemParam;
import com.ys.mail.model.vo.OmsCartItemVO;

import java.util.HashMap;
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
     * 添加商品进购物车
     * @param skuId skuId
     * @param quantity 数量
     * @return 返回值
     */
    CommonResult<Boolean> add(Long skuId,Integer quantity);

//    /**
//     * 添加商品到购物车
//     * @param
//     * @return 返回值
//     */
//    List<OmsCartItem> getCarItem();

    /**
     * 查询出分组的购物车基本信息
     * @param userId 用户id
     * @return 返回值
     */
    List<OmsCartItem> list(Long userId);

    /**
     * 批量删除购物车信息
     * @param ids sku集合id
     * @return 返回值
     */
    boolean removeBySkuId(List<Long> ids);

    /**
     * 查询列表
     * @param ids 集合id
     * @return 返回值
     */
    BatchBuyProductDTO batchProduct(List<Long> ids);

    /**
     * 修改库存数量
     * @param skuId 购物车主键id
     * @param num 数量
     * @return 返回值
     */
    boolean update(Long skuId,Integer num);

    /**
     * 购物车创建订单
     * @param param 参数
     * @return 返回值
     */
    GenerateOrderBO createOrder(CreateOrderParam param);
}
