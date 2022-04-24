package com.ys.mail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.constant.FigureConstant;
import com.ys.mail.entity.*;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.PmsProductMapper;
import com.ys.mail.mapper.UmsPartnerMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.BuyProductDTO;
import com.ys.mail.model.dto.ProductCollectDTO;
import com.ys.mail.model.dto.ProductInfoDTO;
import com.ys.mail.model.param.BathGenerateOrderParam;
import com.ys.mail.model.param.ConGenerateOrderParam;
import com.ys.mail.model.param.ProductParam;
import com.ys.mail.model.po.BuyProductPO;
import com.ys.mail.model.po.MebSkuPO;
import com.ys.mail.model.po.ProductPO;
import com.ys.mail.service.*;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdGenerator;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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

    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UmsAddressService addressService;
    @Autowired
    private PmsSkuStockService skuStockService;
    @Autowired
    private OmsOrderService orderService;
    @Autowired
    private OmsOrderItemService orderItemService;
    @Autowired
    private OmsCartItemService omsCartItemService;
    @Autowired
    private UmsProductCollectService productCollectService;
    @Autowired
    private UmsPartnerMapper partnerMapper;

    @Autowired
    private UmsRealNameService umsRealNameService;

    @Autowired
    private UmsUserInviteRuleService umsUserInviteRuleService;

    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.homeProductType}")
    private String redisKeyHomeProductType;
    @Value("${redis.expire.homePage}")
    private Long redisExpireHomePage;

    @Override
    public List<PmsProduct> getAllProduct(Long productId) {
        return productMapper.selectAllProduct(productId);
    }

    @Override
    public List<PmsProduct> getProductPick(Long pdtCgyId) {
        return productMapper.selectProductPick(pdtCgyId);
    }

    @Override
    public List<PmsProduct> getHomeProductType(Long productId, Integer homeProductType) {

        return productMapper.selectHomeProductType(productId, homeProductType);
    }

    @Override
    public CommonResult<String> collectProduct(Long productId, Long pdtCollectId) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        long result = 0;
        boolean b = false;
        QueryWrapper<UmsProductCollect> eq = new QueryWrapper<UmsProductCollect>().eq("product_id", productId)
                                                                                  .eq("user_id", userId);
        UmsProductCollect one = productCollectService.getOne(eq);
        if (pdtCollectId.equals(NumberUtils.LONG_ZERO) && BlankUtil.isEmpty(one)) {
            // 新增
            result = IdWorker.generateId();
            b = productMapper.saveCollectProduct(result, userId, productId);
        } else if (!BlankUtil.isEmpty(one)) {
            b = productMapper.delCollectProduct(pdtCollectId);
        } else {
            return CommonResult.failed(CommonResultCode.ERR_USER_PRODUCT_COLLECT);
        }
        return b ? CommonResult.success(Long.toString(result)) : CommonResult.failed(CommonResultCode.ERR_USER_PRODUCT_COLLECT);
    }

    @Override
    public ProductInfoDTO getProductInfo(Long productId, Boolean flag) {
        ProductInfoDTO productInfoDTO = productMapper.selectPdtInfo(productId, flag);
        Long pdtCollectId = productMapper.selectByUserIdOrPdtId(productId, UserUtil.getCurrentUser().getUserId());
        productInfoDTO.setPdtCollectId(BlankUtil.isEmpty(pdtCollectId) ? NumberUtils.LONG_ZERO : pdtCollectId);
        return productInfoDTO;
    }

    @Override
    public List<ProductCollectDTO> getAllCollectProduct(Long pdtCollectId) {

        return productMapper.selectAllCollectProduct(UserUtil.getCurrentUser().getUserId(), pdtCollectId);
    }

    @Override
    public CommonResult<Boolean> batchCollectDel(List<Long> ids) {
        boolean b = productMapper.batchCollectDel(ids);
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @Override
    public BuyProductDTO getBuyProduct(Long skuStockId, Integer quantity, Boolean flag, Long addressId) {
        // TODO 商品id
        BuyProductPO buyProduct = productMapper.selectBuyProduct(skuStockId, quantity, flag);
        if (BlankUtil.isEmpty(buyProduct)) {
            return null;
        }
        buyProduct.setQuantity(quantity);

        // 获取最近的个人地址或者默认的
        Long userId = UserUtil.getCurrentUser().getUserId();
        UmsAddress umsAddress = null;
        if (BlankUtil.isNotEmpty(addressId)) {
            umsAddress = addressService.getById(addressId);
        }
        if (BlankUtil.isEmpty(umsAddress)) {
            umsAddress = addressService.getRecentAddressOrDefault(userId, null, null);
        }

        // 返回结果
        return BuyProductDTO.builder()
                            .address(umsAddress)
                            .buyProductPo(buyProduct)
                            .build();
    }

    /**
     * 兑换礼品->跳转到订单确认时查询
     *
     * @param productId 礼品ID
     * @return 用户信息与商品信息
     */
    @Override
    public BuyProductDTO getBuyProduct(Long productId) {
        QueryWrapper<PmsProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId).eq("is_publish_status", NumberUtils.INTEGER_ONE).eq("promotion_type", 7)
               .last("LIMIT 1");
        PmsProduct pmsProduct = this.getOne(wrapper);

        if (BeanUtil.isNotEmpty(pmsProduct)) {
            BuyProductPO buyProductPO = new BuyProductPO();
            buyProductPO.setProductId(pmsProduct.getProductId());
            buyProductPO.setPdtCgyId(pmsProduct.getPdtCgyId());
            buyProductPO.setProductName(pmsProduct.getProductName());
            buyProductPO.setPic(pmsProduct.getPic());
            buyProductPO.setQuantity(NumberUtils.INTEGER_ZERO);
            buyProductPO.setPrice(NumberUtils.LONG_ZERO);
            buyProductPO.setSkuStockId(NumberUtils.LONG_ZERO);
            buyProductPO.setSpData("");

            return BuyProductDTO.builder()
                                .buyProductPo(buyProductPO)
                                .address(addressService.getByUserId(UserUtil.getCurrentUser().getUserId())).build();
        }
        return null;
    }

    @Override
    public List<PmsProduct> getHandpickProduct(Long productId, String pdtCgyId) {
        return productMapper.selectHandpickProduct(productId, Long.valueOf(pdtCgyId));
    }

    @Override
    public synchronized CommonResult<GenerateOrderBO> generateOrder(ConGenerateOrderParam param) {
        // 先计算价格是否正确,查出来一个对象, 对比价格,对比库存,前端随便输入也不行进行前后端一致性判断
        /**
         * 是否启用会员价,一种情况null,false,返回回来的会员价格,
         * 传过来为true才能使用会员价,
         */
        MebSkuPO po = skuStockService.getBySkuId(Long.valueOf(param.getSkuStockId()));
        Optional.ofNullable(po).orElseThrow(() -> new ApiException(BusinessErrorCode.GOODS_NOT_EXIST));
        UmsUser user = UserUtil.getCurrentUser();
        Long price = param.getPrice() * param.getQuantity();
        if (BlankUtil.isNotEmpty(param.getFlag()) && param.getFlag()) {
            if (user.getRoleId().equals(NumberUtils.INTEGER_ONE)) {
                BigDecimal multiply = po.getDisCount().multiply(new BigDecimal(po.getPrice()));
                if (multiply.compareTo(new BigDecimal(param.getMebPrice())) != NumberUtils.INTEGER_ZERO) {
                    return CommonResult.failed(BusinessErrorCode.ERR_PRODUCT_PRICE);
                }
            } else {
                return CommonResult.failed(BusinessErrorCode.NOT_ROLE_ONE_USER);
            }
            price = param.getMebPrice() * param.getQuantity();
        } else {
            if (!price.equals(po.getPrice() * param.getQuantity())) {
                return CommonResult.failed(BusinessErrorCode.ERR_PRODUCT_PRICE);
            }
        }
        if (po.getStock() < param.getQuantity()) {
            return CommonResult.failed(BusinessErrorCode.GOODS_STOCK_EMPTY);
        }
        OmsOrder order = new OmsOrder();
        BeanUtils.copyProperties(param, order);
        // 订单id
        Long orderId = IdWorker.generateId();
        String orderSn = IdGenerator.INSTANCE.generateId();
        order.setOrderId(orderId);
        order.setOrderSn(orderSn);
        order.setUserId(user.getUserId());
        order.setTotalAmount(price);
        order.setPayAmount(price);
        order.setOrderType(NumberUtils.INTEGER_ZERO);
        order.setAutoConfirmDay(param.getOrderSelect()
                                     .equals(NumberUtils.INTEGER_ONE) ? FigureConstant.CONFIRM_DELIVERY_DAY : null);
        try {
            // 为true生成item的子订单
            if (!orderService.save(order)) {
                return CommonResult.failed(BusinessErrorCode.ORDER_STOCK_FAILED);
            }
            OmsOrderItem orderItem = OmsOrderItem.builder()
                                                 .orderId(orderId)
                                                 .productId(Long.valueOf(param.getProductId()))
                                                 .orderItemId(IdWorker.generateId())
                                                 .orderSn(orderSn)
                                                 .productPic(param.getPic())
                                                 .pdtCgyId(Long.valueOf(param.getPdtCgyId()))
                                                 .productName(param.getProductName())
                                                 .productPrice(price)
                                                 .productQuantity(param.getQuantity())
                                                 .productSkuId(po.getSkuStockId())
                                                 .productAttr(po.getSpData())
                                                 .realAmount(Objects.isNull(param.getMebPrice()) ? NumberUtils.LONG_ZERO : price)
                                                 .build();
            if (!orderItemService.save(orderItem)) {
                throw new ApiException(BusinessErrorCode.ORDER_STOCK_FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(e.getMessage());
        }

        // 订单成功后吧订单编号和金额传给前端
        productMapper.updateSale(Long.valueOf(param.getProductId()), param.getQuantity());
        skuStockService.updateSale(Long.valueOf(param.getSkuStockId()), param.getQuantity());

        return CommonResult.success(new GenerateOrderBO(orderSn, price.toString()));
    }

   /* @Async
    public void updateSale(Long productId,Integer quantity,Long skuStockId){
        // 测试一下线程,前端详情页面打不开
        System.out.println("TaskA thread name->" + Thread.currentThread().getName() + "start");
        productMapper.updateSale(productId, quantity);
        skuStockService.updateSale(skuStockId, quantity);
        System.out.println("TaskA thread name->" + Thread.currentThread().getName() + "end");
    }*/

    @Override
    public List<PmsProduct> getProductOfHandpickRecommend(Long productId, Integer pageSize) {
        return productMapper.selectProductOfHandpickRecommend(productId, pageSize);
    }

    @Override
    public CommonResult<Boolean> bathGenerateOrder(BathGenerateOrderParam param) {
        boolean save;
        try {
            OmsOrder order = new OmsOrder();
            BeanUtils.copyProperties(param, order);
            // 订单id
            Long orderId = IdWorker.generateId();
            String orderSn = IdGenerator.INSTANCE.generateId();
            order.setOrderId(orderId);
            order.setOrderSn(orderSn);
            order.setUserId(UserUtil.getCurrentUser().getUserId());
            order.setOrderType(NumberUtils.INTEGER_ZERO);
            order.setPayType(NumberUtils.INTEGER_ZERO);
            order.setOrderStatus(NumberUtils.INTEGER_ZERO);
            order.setDeleteStatus(NumberUtils.INTEGER_ZERO);
            order.setBillType(NumberUtils.INTEGER_ZERO);
            order.setAutoConfirmDay(param.getOrderSelect()
                                         .equals(NumberUtils.INTEGER_ONE) ? FigureConstant.CONFIRM_DELIVERY_DAY : null);

            long totalAmount = 0L;//订单总金额
            long payAmount = 0L;//应付金额
            List<OmsOrderItem> orderItems = new ArrayList<>();
            //遍历购物车内的商品数据
            List<OmsCartItem> cartItems = omsCartItemService.listByIds(param.getOmsCartItemIds());
            for (OmsCartItem item : cartItems) {
                //查询商品
                PmsProduct product = productMapper.selectOne(new QueryWrapper<PmsProduct>()
                        .eq("product_id", item.getProductId()).eq("deleted", NumberUtils.INTEGER_ZERO));
                if (BlankUtil.isEmpty(product)) return CommonResult.failed(BusinessErrorCode.GOODS_NOT_EXIST);

                PmsSkuStock sku = skuStockService.getById(item.getProductSkuId());
                if (BlankUtil.isEmpty(sku)) return CommonResult.failed(BusinessErrorCode.GOODS_STOCK_EMPTY);

                //对比库存
                if (sku.getStock() < item.getQuantity())
                    return CommonResult.failed(BusinessErrorCode.GOODS_STOCK_EMPTY);

                //计算单价
                long productPrice = product.getPrice() * item.getQuantity();//原始商品价格
                long skuPrice = sku.getPrice() * item.getQuantity();//库存价格

                OmsOrderItem orderItem = OmsOrderItem.builder()
                                                     .orderId(orderId)
                                                     .productId(item.getProductId())
                                                     .orderItemId(IdWorker.generateId())
                                                     .orderSn(orderSn)
                                                     .productPic(sku.getPic())
                                                     .pdtCgyId(product.getPdtCgyId())
                                                     .productName(product.getProductName())
                                                     .productPrice(sku.getPrice())
                                                     .productQuantity(item.getQuantity())
                                                     .productSkuId(item.getProductSkuId())
                                                     .productAttr(item.getProductAttr())
                                                     .productSkuCode(sku.getSkuCode())
                                                     .build();
                orderItems.add(orderItem);

                totalAmount += productPrice;
                payAmount += skuPrice;
            }

            //对比总价格
            if (!param.getPayAmount().equals(payAmount))
                return CommonResult.failed(BusinessErrorCode.ERR_PRODUCT_PRICE);

            order.setTotalAmount(totalAmount);
            order.setPayAmount(payAmount);

            orderItemService.saveBatch(orderItems);
            //删除购物车的商品
            omsCartItemService.batchDelCart(param.getOmsCartItemIds());
            save = orderService.save(order);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(e.getMessage());
        }
        return save ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @Override
    public List<PmsProduct> getGift() {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<PmsProduct> qwp = new QueryWrapper<>();
        qwp.eq("promotion_type", 7);
        return productMapper.selectList(qwp);
    }

    @Override
    public List<PmsProduct> partnerProduct(Long productId, Integer pageSize) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        UmsPartner umsPartner = partnerMapper.selectOne(Wrappers.<UmsPartner>lambdaQuery()
                                                                .eq(UmsPartner::getUserId, userId)
                                                                .eq(UmsPartner::getDeleted, 0));
        if (BlankUtil.isEmpty(umsPartner)) {
            return new ArrayList<>(0);
        }
        // 得到合伙人id
        Long partnerId = umsPartner.getPartnerId();
        return productMapper.partnerProduct(partnerId, productId, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByOrderId(Long orderId) {
        return productMapper.updateByOrderId(orderId);
    }

    @Override
    public List<ProductPO> searchAllPdtType(ProductParam param) {
        return productMapper.selectAllPdtType(param);
    }

    @Override
    public List<PmsProduct> selectMebs() {
        return productMapper.selectMebs();
    }
}
