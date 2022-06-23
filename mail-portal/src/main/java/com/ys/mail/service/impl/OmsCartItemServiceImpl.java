package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.entity.UmsAddress;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.mapper.OmsCartItemMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.BatchBuyProductDTO;
import com.ys.mail.model.param.CreateOrderParam;
import com.ys.mail.model.po.ProductAndBrandPO;
import com.ys.mail.service.*;
import com.ys.mail.util.IdGenerator;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    @Autowired
    private OmsCartItemMapper cartItemMapper;
    @Autowired
    private UmsAddressService addressService;
    @Autowired
    private OmsOrderService orderService;
    @Autowired
    private OmsOrderItemService orderItemService;
    @Autowired
    private PmsProductService pmsProductService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<Boolean> add(Long skuId, Integer quantity) {
        // TODO 添加商品进入,改动比较小,一种情况
        ApiAssert.noValue(cartItemMapper.selectByNum(skuId, quantity), BusinessErrorCode.GOODS_STOCK_EMPTY);
        OmsCartItem item = cartItemMapper.selectByCartInfo(skuId, quantity);
        ApiAssert.noValue(item, BusinessErrorCode.GOODS_NOT_EXIST);
        item.setCartItemId(IdWorker.generateId());
        item.setUserId(UserUtil.getCurrentUser().getUserId());
        item.setQuantity(quantity);
        return cartItemMapper.insertOrUpdate(item) ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Long skuId, Integer num) {
        ApiAssert.noValue(cartItemMapper.selectByNum(skuId, num), BusinessErrorCode.GOODS_STOCK_EMPTY);
        return cartItemMapper.update(skuId, num, UserUtil.getCurrentUser().getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GenerateOrderBO createOrder(CreateOrderParam param) {
        // 已经判断好了,第一判断金额是否一致,第二判断,查默认地址,查集合价格,生成订单接口需要再判断下库存,查询出来只要有一个为null就是库存不足
        Long userId = UserUtil.getCurrentUser().getUserId();
        UmsAddress address = addressService.getById(param.getAddressId());
        ApiAssert.noValue(address, BusinessErrorCode.ADDRESS_NULL);
        List<OmsCartItem> items = cartItemMapper.selectBySkuId(param.getCarts(), userId);
        ApiAssert.noValue(items, BusinessErrorCode.GOODS_NOT_NUM_EXIST);
        ApiAssert.noEq(items.size(), param.getCarts().size(), BusinessErrorCode.GOODS_STOCK_EMPTY);
        Long sumPrice = items.stream().filter(Objects::nonNull)
                             .reduce(NumberUtils.LONG_ZERO, (sum, p) -> sum += p.getPrice(), Long::sum);
        Long totalAmount = param.getTotalAmount();
        ApiAssert.noEq(totalAmount, sumPrice, BusinessErrorCode.ERR_PRODUCT_PRICE);
        // 获取商品品牌信息
        List<Long> productIds = items.stream().map(OmsCartItem::getProductId).collect(Collectors.toList());
        List<ProductAndBrandPO> pos = pmsProductService.selectProductBrandInfoByIds(productIds);

        // 生成订单编号,金额,生成订单
        String orderSn = IdGenerator.INSTANCE.generateId();
        Long orderId = IdWorker.generateId();
        List<Long> ids = IdWorker.generateIds(items.size());
        List<OmsOrderItem> orderItems = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        items.stream().filter(Objects::nonNull).forEach(
                vo -> {
                    atomicInteger.incrementAndGet();
                    // 构建商品详情对象
                    OmsOrderItem.OmsOrderItemBuilder builder = OmsOrderItem.builder()
                                                                           .orderItemId(ids.get(atomicInteger.get() - NumberUtils.INTEGER_ONE))
                                                                           .productId(vo.getProductId())
                                                                           .pdtCgyId(vo.getPdtCgyId())
                                                                           .productPic(vo.getProductPic())
                                                                           .productName(vo.getProductName())
                                                                           .productPrice(vo.getPrice())
                                                                           .productQuantity(vo.getQuantity())
                                                                           .productSkuId(vo.getProductSkuId())
                                                                           .productSkuCode(vo.getProductSkuCode())
                                                                           .productAttr(vo.getProductAttr())
                                                                           .orderId(orderId)
                                                                           .orderSn(orderSn);

                    // 获取品牌等信息
                    Optional<ProductAndBrandPO> optional = pos.stream()
                                                              .filter(po -> po.getProductId().equals(vo.getProductId()))
                                                              .findAny();
                    if (optional.isPresent()) {
                        ProductAndBrandPO po = optional.get();
                        builder.productSn(po.getProductSn())
                               .productBrand(po.getBrandName())
                               .pdtAttributeCgyName(po.getName());
                    }

                    // 保存
                    OmsOrderItem orderItem = builder.build();
                    orderItems.add(orderItem);
                }
        );
        OmsOrder order = OmsOrder.builder()
                                 .orderId(orderId)
                                 .orderSn(orderSn)
                                 .userId(userId)
                                 .totalAmount(totalAmount)
                                 .payAmount(totalAmount)
                                 .orderType(NumberUtils.INTEGER_ZERO)
                                 .receiverName(address.getContacts())
                                 .receiverPhone(address.getPhone())
                                 .receiverProvince(address.getProvince())
                                 .receiverCity(address.getCity())
                                 .receiverRegion(address.getCounty())
                                 .receiverAddress(address.getClientAddress())
                                 .cpyType(NumberUtils.BYTE_ZERO)
                                 .orderNote(param.getOrderNote())
                                 .build();
        List<Long> collect = items.stream().map(OmsCartItem::getCartItemId).collect(Collectors.toList());
        // TODO 库存还没减掉
        return orderService.save(order) &&
                orderItemService.saveBatch(orderItems) &&
                removeByIds(collect) ? new GenerateOrderBO(orderSn, totalAmount.toString()) : null;
    }

}
