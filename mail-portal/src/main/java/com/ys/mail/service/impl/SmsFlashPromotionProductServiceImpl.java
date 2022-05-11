package com.ys.mail.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.RabbitMqSmsConfig;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.constant.FigureConstant;
import com.ys.mail.entity.*;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.*;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.MapQuery;
import com.ys.mail.model.bo.FlashPromotionProductBO;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.*;
import com.ys.mail.model.map.RedisGeoDTO;
import com.ys.mail.model.mq.MSOrderCheckDTO;
import com.ys.mail.model.param.GenerateOrderParam;
import com.ys.mail.model.po.MyStorePO;
import com.ys.mail.model.query.QueryQuickBuy;
import com.ys.mail.model.query.QuickBuyProductQuery;
import com.ys.mail.model.vo.NearbyStoreProductVO;
import com.ys.mail.model.vo.OrderItemDetailsVO;
import com.ys.mail.service.*;
import com.ys.mail.util.*;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-12 16:01
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SmsFlashPromotionProductServiceImpl extends ServiceImpl<SmsFlashPromotionProductMapper, SmsFlashPromotionProduct> implements SmsFlashPromotionProductService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SmsFlashPromotionProductServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SmsFlashPromotionProductMapper flashPromotionProductMapper;
    @Autowired
    private SmsFlashPromotionMapper flashPromotionMapper;
    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private UmsUserMapper umsUserMapper;
    @Autowired
    private SysTemSettingMapper sysTemSettingMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private UmsAddressService addressService;
    @Autowired
    private OmsOrderService orderService;
    @Autowired
    private OmsOrderItemService orderItemService;
    @Autowired
    private PmsSkuStockService skuStockService;
    @Autowired
    private SmsFlashPromotionHistoryMapper flashPromotionHistoryMapper;
    @Autowired
    private OmsVerificationOrderMapper verificationOrderMapper;
    @Autowired
    private PmsVerificationCodeMapper verificationCodeMapper;
    @Autowired
    private UmsPartnerMapper partnerMapper;
    @Autowired
    private UmsPartnerService umsPartnerService;
    @Autowired
    private UmsIncomeMapper umsIncomeMapper;
    @Autowired
    private SmsFlashPromotionHistoryService historyService;
    @Autowired
    private SmsProductStoreService smsProductStoreService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;


    @Value("${redis.database}")
    private String redisDatabase;

    @Override
    public CommonResult<List<FlashPromotionProductDTO>> getNewestSecond() {
        List<FlashPromotionProductDTO> dtoList = flashPromotionProductMapper.selectNewestSecond();
        return CommonResult.success(dtoList);
    }

    @Override
    public List<FlashPromotionProductDTO> getFlashMessage(Boolean more, Long integralId) {
        List<FlashPromotionProductDTO> dtoList = flashPromotionProductMapper.getFlashMessage(more, integralId);
        return dtoList;
    }

    @Override
    public FlashPromotionProductDTO getFlashPublishMessage(Long flashPromotionPdtId) {

        FlashPromotionProductDTO dto = flashPromotionProductMapper.getFlashPublishMessage(flashPromotionPdtId, new Date());
        return dto;
    }

    @Override
    public List<FlashPromotionProductDTO> getUserFlashProduct(Boolean more, Long integralId, Byte cpyType) {

        return flashPromotionProductMapper.getUserFlashProduct(more, integralId, cpyType, UserUtil.getCurrentUser()
                                                                                                  .getUserId());
    }

    @Override
    public CommonResult<Boolean> addUserFlashProduct(Long flashPromotionPdtId) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        // 所属用户和id校验
        SqlLambdaQueryWrapper<SmsFlashPromotionProduct> wrapper = new SqlLambdaQueryWrapper<>();
        wrapper.eq(SmsFlashPromotionProduct::getFlashPromotionPdtId, flashPromotionPdtId)
               .eq(SmsFlashPromotionProduct::getUserId, userId);
        SmsFlashPromotionProduct smsFlashPromotionProduct = flashPromotionProductMapper.selectOne(wrapper);
        ApiAssert.noValue(smsFlashPromotionProduct, BusinessErrorCode.FLASH_PRODUCT_NO_EXIST);

        // 状态校验：只有当秒杀状态为3才能上架
        Integer flashProductStatus = smsFlashPromotionProduct.getFlashProductStatus();
        boolean condition = SmsFlashPromotionProduct.FlashProductStatus.THREE.key().equals(flashProductStatus);
        ApiAssert.isFalse(condition, CommonResultCode.ILLEGAL_REQUEST);

        // 过期时间校验
        boolean expireTime = DateTool.isExpireTime(smsFlashPromotionProduct.getExpireTime());
        ApiAssert.isTrue(expireTime, BusinessErrorCode.ERR_DATE_EXPIRE);

        // 填充信息
        smsFlashPromotionProduct.setIsPublishStatus(true);
        // 秒杀商品状态为 3上架
        smsFlashPromotionProduct.setFlashProductStatus(2);
        //上架操作
        smsFlashPromotionProduct.setPublisherId(smsFlashPromotionProduct.getUserId());
        // 平台
        Byte cpyType = smsFlashPromotionProduct.getCpyType();

        // 查询最近场次信息
        SecondProductDTO secondProductDTO = flashPromotionMapper.selectCpyTypeOne(cpyType);
        // 更新限时购表id
        boolean result = false;
        if (secondProductDTO != null) {
            // 关联用户店铺信息
            SmsProductStore reviewed = smsProductStoreService.getReviewed();
            ProductStoreObjDTO storeObjDTO = new ProductStoreObjDTO();
            BeanUtils.copyProperties(reviewed, storeObjDTO);
            smsFlashPromotionProduct.setPdtStoreObj(JSON.toJSONString(storeObjDTO));

            // 关联场次ID
            smsFlashPromotionProduct.setFlashPromotionId(secondProductDTO.getFlashPromotionId());

            // 更新到数据库
            result = updateById(smsFlashPromotionProduct);

            // 清理缓存
            redisService.keys(redisDatabase + ":home:*");

        }

        //返回结果
        return result ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @Override
    public List<FlashPromotionProductDTO> getFlashProductMesg(Long integralId) {
        List<FlashPromotionProductDTO> dto = flashPromotionProductMapper.getFlashProductMesg(integralId, UserUtil
                .getCurrentUser().getUserId());
        QueryWrapper<SysTemSetting> qw = new QueryWrapper<>();
        qw.eq("tem_type", 0);
        SysTemSetting sysTemSetting = sysTemSettingMapper.selectOne(qw);
        for (FlashPromotionProductDTO entity : dto) {
            // 返回设置,不能用前端写死
            BigDecimal bg = new BigDecimal(entity.getFlashPromotionPrice());
            /*BigDecimal merchantOrder = bg.multiply(sysTemSetting.getMerchantOrder()).multiply(BigDecimal.valueOf(0.01));
            BigDecimal systemOrder = bg.multiply(sysTemSetting.getSystemOrder()).multiply(BigDecimal.valueOf(0.01));
            entity.setMerchantOrder(merchantOrder);
            entity.setSystemOrder(systemOrder);*/
            entity.setMerchantOrder(sysTemSetting.getMerchantOrder());
            entity.setSystemOrder(sysTemSetting.getSystemOrder());
        }
        return dto;
    }

    @Override
    public QuickBuyProductInfoDTO quickBuyProductInfo(QuickBuyProductQuery qo) {
        QuickBuyProductInfoDTO infoDTO = flashPromotionProductMapper.quickBuyProductInfo(qo);
        if (!BlankUtil.isEmpty(infoDTO)) {
            Long userId = UserUtil.getCurrentUser().getUserId();
            Long pdtCollectId = productMapper.selectByUserIdOrPdtId(Long.valueOf(qo.getProductId()), userId);
            infoDTO.setPdtCollectId(BlankUtil.isEmpty(pdtCollectId) ? NumberUtils.LONG_ZERO : pdtCollectId);
            infoDTO.setAddress(addressService.getByUserId(userId));
            Long partnerId = infoDTO.getPartnerId();
            UmsPartner umsPartner = partnerMapper.selectById(partnerId);
            if (ObjectUtils.isNotEmpty(umsPartner)) {
                infoDTO.setProvince(umsPartner.getProvince());
                infoDTO.setCity(umsPartner.getCity());
                infoDTO.setRegion(umsPartner.getRegion());
                infoDTO.setShopAddress(umsPartner.getAddress());
            }
        }
        return infoDTO;
    }

    @Override
    public QuickProductDTO getQuickBuy(QueryQuickBuy qo) {
        return new QuickProductDTO(addressService.getByUserId(UserUtil.getCurrentUser().getUserId()),
                flashPromotionProductMapper.selectQuickBuy(qo));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized CommonResult<GenerateOrderBO> quickGenerateOrder(GenerateOrderParam param) {
        LOGGER.info("---秒杀抢购开始---");
        LOGGER.info("请求参数为:{}", JSON.toJSONString(param));
        Long userId = UserUtil.getCurrentUser().getUserId();

        LOGGER.info("秒杀抢购下单用户id:{}", userId);
        Long flashPromotionId = param.getFlashPromotionId();

        final String key = "BUY_LIMIT:" + flashPromotionId + ":" + userId;
        // 查询场次表结束时间
        SmsFlashPromotion smsFlashPromotion = flashPromotionMapper.selectById(flashPromotionId);
        if (BlankUtil.isEmpty(smsFlashPromotion)) {
            return CommonResult.failed(BusinessErrorCode.GOODS_NOT_EXIST);
        }
        Date endTimeDate = smsFlashPromotion.getEndTime();
        Date nowDate = new Date();
        long endTimeL = endTimeDate.getTime();
        long nowTimeL = nowDate.getTime();
        long overdueL = endTimeL - nowTimeL;
        // 查询手底下人有多少交了99元数量  payment_type
        Integer auserNumber = umsUserMapper.findAdvancedUsersNumber(userId);
        Integer purchasesNumber = 5 + (auserNumber / 2);
        // 获取 已下单次数
        Integer countRedis = (Integer) redisTemplate.opsForValue().get(key);
        // 获取场次下 该用户下单次数
        if (overdueL <= 0L) {
            return CommonResult.failed("秒杀场次已经结束");
        }

        // 暂时先实现功能,再用锁,暂时先这样后面再优化
        // todo: 查询产品下 类随机 取一个flash_promotion_pdt_id
        if (BlankUtil.isEmpty(param.getFlashPromotionId())) {
            return CommonResult.validateFailed("flashPromotionId不能为空");
        }
        Long divisionPrice = param.getPrice();//param.getQuantity();
        Byte cpyType = param.getCpyType();
        LOGGER.info("单个产品价格:{}", divisionPrice);
        // 需在传递的 flashPromotionId 活动时间内
        SmsFlashPromotionProduct promotionProduct = flashPromotionProductMapper.selectGroupFlashPromotionCount(param.getProductId(), divisionPrice, param.getFlashPromotionId(), cpyType);

        // 用户不能购买自己上架的产品(当随机到的产品为自己上传的，则不能进行秒杀)
        Long productUserId = promotionProduct.getUserId();
        if (BlankUtil.isNotEmpty(productUserId)) {
            ApiAssert.isTrue(productUserId.equals(userId), BusinessErrorCode.FLASH_PRODUCT_NO_BUY);
        }

        // 扣减缓存中的库存
        if (countRedis == null) {
            redisTemplate.opsForValue().set(key, 0, overdueL, TimeUnit.MILLISECONDS);
            countRedis = 0;
        }
        if (purchasesNumber - countRedis <= 0) {
            return CommonResult.failed("当前场次购买次数达到上限");
        }

        Long price = param.getPrice();
        Integer quantity = param.getQuantity();
        if (BlankUtil.isEmpty(promotionProduct)) {
            return CommonResult.failed(BusinessErrorCode.GOODS_NOT_EXIST);
        } else if (!price.equals(promotionProduct.getFlashPromotionPrice() * param.getQuantity())) {
            return CommonResult.failed(BusinessErrorCode.ERR_PRODUCT_PRICE);
        } else if (promotionProduct.getFlashPromotionCount() < quantity) {
            return CommonResult.failed(BusinessErrorCode.GOODS_STOCK_EMPTY);
        }
        OmsOrder order = new OmsOrder();
        BeanUtils.copyProperties(param, order);
        // 库存充足 查询 当前价格下 flash_promotion_pdt_id 对应的 库存数
        List<SmsFlashPromotionProduct> flashPromotionProductList = flashPromotionProductMapper.selectGroupFlashPromotionCountList(param.getProductId(), divisionPrice, param.getFlashPromotionId());
        // todo: 取第一个
        SmsFlashPromotionProduct flashPromotionProduct = flashPromotionProductList.get(0);
        // todo: 设置最后  key:id value:扣减库存数
        Map<Long, Integer> map = new HashMap<>(flashPromotionProductList.size());
        // todo: 将第一个先put 进去
        map.put(flashPromotionProduct.getFlashPromotionPdtId(), flashPromotionProduct.getFlashPromotionCount());
        // todo: 当库存不满足时进行的处理
        if (quantity > flashPromotionProduct.getFlashPromotionCount()) {
            // 统计第一个库存数
            Integer count = flashPromotionProduct.getFlashPromotionCount();
            // 循环统计库存数
            for (int i = 1; i < flashPromotionProductList.size(); ++i) {
                SmsFlashPromotionProduct product = flashPromotionProductList.get(i);
                Integer flashPromotionCount = product.getFlashPromotionCount();
                count += flashPromotionCount;
                // 先添加 可能会被更新
                map.put(product.getFlashPromotionPdtId(), flashPromotionCount);
                if (count > quantity) {
                    // 此次循环大于了要扣减库存数 要进行库存数扣减更新   计算出来大于的值
                    map.put(product.getFlashPromotionPdtId(), flashPromotionCount - (count - quantity));
                    break;
                } else if (count.equals(quantity)) {
                    // 等于就直接跳出循环 因为做了先put 所以无需任何操作
                    break;
                }
            }
        } else {
            map.put(flashPromotionProduct.getFlashPromotionPdtId(), quantity);
        }
        // 查询合伙人id,添加到订单
        PmsProduct pmsProduct = productMapper.selectById(param.getProductId());
        // 订单id 防止id重复循环了一下
        Long orderId = IdWorker.generateId();
        String orderSn = IdGenerator.INSTANCE.generateId();
        order.setOrderId(orderId);
        order.setOrderSn(orderSn);
        order.setUserId(userId);
        order.setTotalAmount(price);
        order.setPartnerPrice(flashPromotionProduct.getPartnerPrice());
        order.setPayAmount(price);
        order.setOrderType(NumberUtils.INTEGER_ONE);
        order.setAutoConfirmDay(param.getOrderSelect()
                                     .equals(NumberUtils.INTEGER_ONE) ? FigureConstant.CONFIRM_DELIVERY_DAY : null);
        order.setCpyType(param.getCpyType());
        order.setPartnerId(pmsProduct.getPartnerId());
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
                                                 .productSkuId(Long.valueOf(param.getSkuStockId()))
                                                 .productAttr(param.getSpData())
                                                 .build();
            if (!orderItemService.save(orderItem)) {
                throw new ApiException(BusinessErrorCode.ORDER_STOCK_FAILED);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            throw new ApiException(e.getMessage());
        }
        // 减掉库存
        productMapper.updateSale(Long.valueOf(param.getProductId()), param.getQuantity());
        // todo: 遗留 不扣减pms_product库存??
        flashPromotionProductMapper.updateSale(map);
        LOGGER.info("此次关联的订单id:{}", orderId);
        LOGGER.info("此次扣减的库存:{}", JSON.toJSONString(map));
        LOGGER.info("---秒杀抢购结束---");

        // 发送 5分钟mq orderId +  产品id+扣减的库存 map
        rabbitTemplate.convertAndSend(RabbitMqSmsConfig.MS_EXCHANGE, RabbitMqSmsConfig.MS_ROUTING_KEY, new MSOrderCheckDTO(orderId, map));

        redisTemplate.opsForValue().increment(key);

        return CommonResult.success(new GenerateOrderBO(orderSn, price.toString()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized CommonResult<Object> confirmReceipt(String orderSn) throws IOException {
        //todo: 效验订单  订单流程不熟 暂定 已付款才能确认收货
        OrderInfoDTO orderInfo = orderService.getOrderInfo(orderSn);
        // 订单状态必须为 已发货 -因为秒杀单-支付宝支付后只会是已发货
        if (BlankUtil.isEmpty(orderInfo) || !OmsOrder.OrderStatus.TWO.key().equals(orderInfo.getOrderStatus())) {
            return CommonResult.failed(BusinessErrorCode.PRODUCT_CONFIRMRECEIPT_FAILED_ORDER_ERROR);
        }
        Long orderId = orderInfo.getOrderId();
        // todo: 如果秒杀逻辑修改可以一次下多个不同商品此处需要修改逻辑!!!
        OmsOrderItem oneByOrderId = orderItemService.getOneByOrderId(orderId);
        Long productSkuId = oneByOrderId.getProductSkuId();
        Long productId = oneByOrderId.getProductId();

        LOGGER.info("=======确认收货 start=======");
        LOGGER.info("通过订单编号查询到的订单信息:{}", JSON.toJSONString(orderInfo));
        Long userId = orderInfo.getUserId();
        LOGGER.info("通过订单得到用户id:{}", userId);
        LOGGER.info("当前调用接口的用户id:{}", UserUtil.getCurrentUser().getUserId());
        GetRequest getRequest = new GetRequest("game_record").id(String.valueOf(userId));
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        // 默认  1 + 0.005+0.038 = 1.043  ----> 2022.02.14 修改为:  1+0.005 = 1.005
        BigDecimal bigDecimal = BigDecimal.valueOf(1.005);
        if (StringUtils.isNotBlank(sourceAsString)) {
            ESGameRecord esGameRecord = JSON.parseObject(sourceAsString, ESGameRecord.class);
            List<GameRecord> gameRecords = esGameRecord.getEarnings();
            if (ObjectUtils.isNotEmpty(gameRecords)) {
                // 此处使用了 订单的支付时间作为查询依据
                Date paymentTime = orderInfo.getPaymentTime();
                Integer ym = Integer.valueOf(new SimpleDateFormat("yyyyMM").format(paymentTime));
                List<GameRecord> collect = gameRecords.stream().filter(ym::equals).collect(Collectors.toList());
                // 若ym年月为找到则说明用默认的来进行增幅
                if (BlankUtil.isNotEmpty(collect)) {
                    GameRecord gameRecord = collect.get(0);
                    Double ratio = gameRecord.getRatio();
                    LOGGER.info("查询到ES用户比例为:{}", ratio);
                    // 使用es 比例           用户比例          +               本金      // 2022.2.14移除平台   +     平台固定涨幅
                    bigDecimal = BigDecimal.valueOf(ratio).add(BigDecimal.valueOf(1));//.add(BigDecimal.valueOf(0.038));
                }
            }
        }
        LOGGER.info("此次涨幅比例为:{}", bigDecimal.doubleValue());
        // 订单总金额  ps:支撑条件为->购买产品数量为 1  否则需要进行计算 总金额/数量
        Long totalAmount = orderInfo.getTotalAmount();
        // 单位为分
        BigDecimal bg = new BigDecimal(totalAmount);
        // 涨幅后金额
        BigDecimal exchangePriceBg = bg.multiply(bigDecimal);
        // 逻辑支撑只能一件一件卖
        LOGGER.info("涨幅前总金额:{},数量为{1},涨幅后金额:{}", bg.longValue(), exchangePriceBg.longValue());
        // 置换后金额  400
        long exchangePrice = 0L;
        long originPrice = totalAmount;
        boolean br = false;
        // 系统固定参数值
        int symNum = 5;
        PmsSkuStock skuStock = new PmsSkuStock();
        skuStock.setProductId(productId);
        skuStock.setSkuStockId(productSkuId);
        if ((br = exchangePriceBg.compareTo(BigDecimal.valueOf(2000000)) >= 0)) {
            LOGGER.info("涨幅后金额大于20000元-进入拆分逻辑");
            // 小数点直接舍弃(老板说的)
            exchangePrice = exchangePriceBg.divide(BigDecimal.valueOf(symNum), 0, RoundingMode.DOWN).longValue();
            LOGGER.info("拆分后-涨幅价格:{}", exchangePrice);
            //  原价       原价格           /  数量
            originPrice = bg.divide(BigDecimal.valueOf(symNum), 0, RoundingMode.DOWN).longValue();
            LOGGER.info("拆分后-原价格{}", originPrice);
            // 2022.2.14 添加 2w 才置换商品
            skuStock = skuStockService.closestExchangePrice(exchangePrice);
        } else {
            LOGGER.info("涨幅后金额小于20000元-无拆分逻辑");
            exchangePrice = exchangePriceBg.longValue();
        }
        if (skuStock != null) {
            LOGGER.info("查询最接近涨幅后价格的产品信息:{}", JSON.toJSONString(skuStock));
            SmsFlashPromotionProduct sfpp = new SmsFlashPromotionProduct();
            // 主键
            sfpp.setFlashPromotionPdtId(IdWorker.generateId());
            // 限时购id
            sfpp.setFlashPromotionId(0L);
            // 限时购场次id
            sfpp.setFlashPromotionSnId(0L);
            // 置换后产品id
            sfpp.setProductId(skuStock.getProductId());
            LOGGER.info("最终入库信息-产品id:{}", skuStock.getProductId());
            // 置换后金额
            sfpp.setFlashPromotionPrice(exchangePrice);
            // 数量
            sfpp.setFlashPromotionCount(br ? symNum : 1);
            // 原数量
            sfpp.setFlashPromotionOriginCount(br ? symNum : 1);
            // 置换原价格
            sfpp.setFlashPromotionOriginPrice(originPrice);
            // 实际收入价
            sfpp.setTotalAmount(exchangePrice);
            LOGGER.info("最终入库信息-原价格-:{},也是当前产品卖出后计算持有者收益使用", originPrice);
            // 限购数量
            sfpp.setFlashPromotionLimit(1);
            // 持有Id
            sfpp.setUserId(orderInfo.getUserId());
            LOGGER.info("最终入库信息-持有Id:{}", orderInfo.getUserId());
            // 发布者Id
            sfpp.setPublisherId(null);
            // 最近产品id
            sfpp.setSkuStockId(skuStock.getSkuStockId());
            // 轮次
            sfpp.setFlashPromotionRound(1);
            // 上架状态 false 0下架  true 1上架
            sfpp.setIsPublishStatus(false);
            // 1卖出 2秒杀 3上架
            sfpp.setFlashProductStatus(3);
            // 所有确认收货到我的店铺都是卖乐吧,卖乐吧就是1
            sfpp.setCpyType(NumberUtils.BYTE_ONE);
            // 保存订单id做查询用
            sfpp.setOrderId(orderInfo.getOrderId());
            // 合伙人原价格      如果置换-则拿  置换后产品价格  否则 拿订单的合伙人原价格
            sfpp.setPartnerPrice(br ? skuStock.getPrice() : orderInfo.getPartnerPrice());
            // true 置换成功
            boolean b = saveOrUpdate(sfpp);

            List<OrderItemDetailsVO> omsOrderItem = orderInfo.getOmsOrderItem();
            SmsFlashPromotionHistory smsFlashPromotionHistory = new SmsFlashPromotionHistory();
            smsFlashPromotionHistory.setHistroyId(IdWorker.generateId());
            // 置换后的产品id
            smsFlashPromotionHistory.setProductId(skuStock.getProductId());
            // 置换前产品id
            smsFlashPromotionHistory.setProductIdParent(omsOrderItem.get(0).getProductId());
            // 置换前产品秒杀价格
            smsFlashPromotionHistory.setProductIdPrice(totalAmount);
            // 秒杀用户id
            smsFlashPromotionHistory.setUserId(orderInfo.getUserId());
            // 图片
            smsFlashPromotionHistory.setProductPic(oneByOrderId.getProductPic());
            // 用户名称
            UmsUser currentUser = UserUtil.getCurrentUser();
            smsFlashPromotionHistory.setNickname(BlankUtil.isEmpty(currentUser.getAlipayName()) ? currentUser.getNickname() : currentUser.getAlipayName());
            // 抢到商品的用户名称
            smsFlashPromotionHistory.setNewNickname(orderInfo.getReceiverName());
            // 持有商品的用户id
            smsFlashPromotionHistory.setNewUserId(currentUser.getUserId());
            smsFlashPromotionHistory.setUserPic(currentUser.getHeadPortrait());
            smsFlashPromotionHistory.setNewUserPic(currentUser.getHeadPortrait());
            smsFlashPromotionHistory.setProductName(oneByOrderId.getProductName());

            flashPromotionHistoryMapper.insert(smsFlashPromotionHistory);
            // 订单改为待评价
            if (b) {
                //orderInfo.setOrderType(7);
                orderService.updateByOrderType(orderInfo.getOrderId());
                confirmReceiptVerification(orderInfo.getOrderSn(), symNum, br);
            } else {
                return CommonResult.failed(BusinessErrorCode.PRODUCT_CONFIRMRECEIPT_FAILED);
            }
        }
        LOGGER.info("=======确认收货 end=======");
        return skuStock != null ? CommonResult.success("确认收货成功") : CommonResult.failed(BusinessErrorCode.PRODUCT_CONFIRMRECEIPT_FAILED);
    }

    @Override
    public SmsFlashPromotionDTO currentPlatformPromotionId(Byte cpyType) {
        return flashPromotionMapper.currentPlatformPromotionId(cpyType);
    }

    @Override
    public HasSoldProductDTO getHasSoldProductInfo(Long orderId) {
        HasSoldProductDTO hasSoldProductDTO = flashPromotionMapper.selectHasSoldProductInfo(orderId);
        if (BlankUtil.isNotEmpty(hasSoldProductDTO)) {
            hasSoldProductDTO.setReceiverPhone(RegularUtil.desensitizePhone(hasSoldProductDTO.getReceiverPhone()));
        }
        return hasSoldProductDTO;
    }

    @SneakyThrows
    @Override
    public synchronized CommonResult<Object> userIncome(Long flashPromotionPdtId) {
        LOGGER.info("========用户结算成功 start========");
        // 收益表数据
        UmsIncome umsIncome = new UmsIncome();
        // 秒杀数据
        SmsFlashPromotionProduct product = flashPromotionProductMapper.selectUserIncomeOneSQL(flashPromotionPdtId);
        // 场次id
        Long flashPromotionId = product.getFlashPromotionId();
        SmsFlashPromotion smsFlashPromotion = flashPromotionMapper.selectById(flashPromotionId);
        if (BlankUtil.isEmpty(smsFlashPromotion)) {
            // 回购异常
            return CommonResult.failed("退回失败-场次信息异常");
        }
        Date endTime = smsFlashPromotion.getEndTime();
        Integer ym = Integer.valueOf(new SimpleDateFormat("yyyyMM").format(endTime));
        LOGGER.info("结算场次日期:{}", ym);
        // 秒杀表.持有人
        Long publisherId = product.getPublisherId();
        // 秒杀表.产品id
        Long productId = product.getProductId();
        // 秒杀表.剩余数量
        Integer flashPromotionCount = product.getFlashPromotionCount();
        // 秒杀表.秒杀金额
        Long flashPromotionPrice = product.getFlashPromotionOriginPrice();
        if (ObjectUtil.isEmpty(product.getFlashProductStatus()) || !product.getFlashProductStatus().equals(4)) {
            return CommonResult.failed("该商品已卖出");
        }
        // 若有为Null  直接跳过
        LOGGER.info("持有人:{},产品id:{},剩余数量:{},秒杀产品原价:{}", publisherId, productId, flashPromotionCount, flashPromotionPrice);
        if (ObjectUtil.isNull(publisherId) || ObjectUtil.isNull(productId) ||
                ObjectUtil.isNull(flashPromotionCount) || flashPromotionCount.equals(0) || ObjectUtil.isNull(flashPromotionPrice)) {
            LOGGER.info("-----结束");
            return CommonResult.success("退回成功");
        }
        // {收益}---------并发
        UmsIncome newest = umsIncomeMapper.selectNewestByUserId(publisherId);

        // 收益表.(最新)今日收益
        Long todayIncome = newest != null ? newest.getTodayIncome() : 0L;
        // 收益表.(最新)总收益
        Long allIncome = newest != null ? newest.getAllIncome() : 0L;
        // 收益表.(最新)结余
        Long balance = newest != null ? newest.getBalance() : 0L;
        // {产品}
        PmsProduct pmsProduct = productMapper.selectById(productId);
        // 产品表.产品名
        String productName = pmsProduct != null ? pmsProduct.getProductName() : "id:" + productId;

        // Bg.(最新)今日收益
        BigDecimal todayIncomeBg = new BigDecimal(todayIncome);
        // Bg.(最新)总收益
        BigDecimal allIncomeBg = new BigDecimal(allIncome);
        // Bg.(最新)结余
        BigDecimal balanceBg = new BigDecimal(balance);
        // Bg.剩余数量
        BigDecimal flashPromotionCountBg = new BigDecimal(flashPromotionCount);
        // Bg.秒杀金额
        BigDecimal flashPromotionPriceBg = new BigDecimal(flashPromotionPrice);

        // es 用户佣金比
        GetRequest getRequest = new GetRequest("game_record").id(publisherId.toString());
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();

        GameRecord gameRecord = new GameRecord();
        if (StringUtils.isBlank(sourceAsString)) {
            // topo: 待改比例  0.005D  用户的
            gameRecord.setRatio(0.005D);
        } else {
            ESGameRecord parse = JSON.parseObject(sourceAsString, ESGameRecord.class);
            List<GameRecord> collect = parse.getEarnings().stream().filter(obj -> obj.getDate().equals(ym))
                                            .collect(Collectors.toList());
            if (collect.size() == NumberUtils.INTEGER_ZERO) {
                gameRecord.setRatio(0.005D);
            } else {
                gameRecord = collect.get(0);
            }
        }

        BigDecimal ratioBg = BigDecimal.valueOf(gameRecord.getRatio()).add(BigDecimal.valueOf(1));
        LOGGER.info("受益人id:{},受益人佣金比例:{}", publisherId, ratioBg.doubleValue());
        // 最终收益                               乘以剩余数量                                计算价格
        BigDecimal income = flashPromotionPriceBg.multiply(flashPromotionCountBg).multiply(ratioBg);
        LOGGER.info("受益人最终收益:{}", income.doubleValue());
        // 此次计算今日收益
        BigDecimal todayIncomeBgAddIncome = todayIncomeBg.add(income);
        // 此次总收益
        BigDecimal allIncomeBgAddIncome = allIncomeBg.add(income);
        // 此次结余
        BigDecimal balanceBgBgAddIncome = balanceBg.add(income);
        // 收益ID
        umsIncome.setIncomeId(IdWorker.generateId());
        // 收益人
        umsIncome.setUserId(publisherId);
        // 收益数
        umsIncome.setIncome(income.longValue());
        // 今日收益
        umsIncome.setTodayIncome(todayIncomeBgAddIncome.longValue());
        // 总收益
        umsIncome.setAllIncome(allIncomeBgAddIncome.longValue());
        // 结余
        umsIncome.setBalance(balanceBgBgAddIncome.longValue());
        // 收益类型  1-秒杀收益
        umsIncome.setIncomeType(1);
        // 0->平台回购
        umsIncome.setOriginType((byte) 0);
        // 来源
        umsIncome.setDetailSource("平台回购-秒杀产品:(" + productName + ")");
        // 备注
        umsIncome.setRemark("平台回购-秒杀产品id:{" + product.getFlashPromotionPdtId() + "},价格:{" + flashPromotionPrice + "},持有人id:{" + publisherId + "},结算比例:{" + ratioBg.doubleValue() + "},数量:{" + flashPromotionCount + "}");
        // 该笔收益添加余额的意思
        umsIncome.setPayType(3);
        // 保存唯一商品主键id
        umsIncome.setFlashPromotionPdtId(product.getFlashPromotionPdtId());
        umsIncomeMapper.insert(umsIncome);
        // 收益计算完成 直接 库存 = 0  状态为已卖出
        flashPromotionProductMapper.updateFlashPromotionPdtId(flashPromotionPdtId);
        LOGGER.info("========用户结算成功 end========");
        return CommonResult.success("退回成功");
    }

    // 合伙人二维码 确认收货逻辑
    public void confirmReceiptVerification(String orderSn, int symNum, boolean br) {
        OmsVerificationOrder verificationOrder = verificationOrderMapper.selectOne(Wrappers
                .<OmsVerificationOrder>lambdaQuery().eq(OmsVerificationOrder::getOrderId, orderSn));
        if (ObjectUtils.isNotEmpty(verificationOrder)) {
            Long verificationId = verificationOrder.getVerificationId();
            PmsVerificationCode verificationCode = verificationCodeMapper.selectById(verificationId);
            verificationCode.setIsStatus(PmsVerificationCode.CODE_STATUS_TWO);
            verificationCodeMapper.updateById(verificationCode);
        }
        if (br) {// br = true 新生成  置为失效
            OmsOrder order = orderService.getOne(new QueryWrapper<OmsOrder>().eq("order_sn", orderSn));
            for (int i = 0; i < symNum; ++i) {
                if (BlankUtil.isNotEmpty(order.getPartnerId())) {
                    // 商品数量
                    Integer count = 1;
                    Long verificationId = IdWorker.generateId();
                    // 插入订单核销表
                    OmsVerificationOrder verificationOrder1 = new OmsVerificationOrder();
                    verificationOrder1.setVerificationOrderId(IdWorker.generateId());
                    verificationOrder1.setVerificationId(verificationId);
                    verificationOrder1.setOrderId(order.getOrderSn());
                    verificationOrder1.setQuantity(count);
                    verificationOrderMapper.insert(verificationOrder1);
                    // 插入核销表
                    PmsVerificationCode verificationCode = new PmsVerificationCode();
                    verificationCode.setVerificationId(verificationId);
                    verificationCode.setPartnerId(order.getPartnerId());
                    verificationCode.setCode(IdGenerator.INSTANCE.cancelId());
                    verificationCode.setIsStatus(PmsVerificationCode.CODE_STATUS_TWO);
                    /* 请修改!!! */
                    verificationCodeMapper.insert(verificationCode);
                }
            }
        }
    }

    @Override
    public MyStorePO getMyStore(Integer pageSize, Byte cpyType) {
        return MyStorePO.builder()
                        .storeDtoS(flashPromotionProductMapper.selectMyStore(pageSize, UserUtil.getCurrentUser()
                                                                                               .getUserId(), cpyType))
                        .msgVO(historyService.getShopMsg())
                        .build();
    }

    @Override
    public List<MyStoreDTO> getAllProduct(Byte cpyType, String flashPromotionPdtId) {
        Long id = BlankUtil.isEmpty(flashPromotionPdtId) ? null : Long.valueOf(flashPromotionPdtId);
        return flashPromotionProductMapper.selectAllProduct(cpyType, id, UserUtil.getCurrentUser().getUserId());
    }

    @Override
    public NearbyStoreProductVO getNearbyStore(Long flashPromotionId, Integer productType, Double radius, MapQuery mapQuery, Long partnerId) {
        String fullKey = redisConfig.fullKey(redisConfig.getKey().getGeo() + ":" + flashPromotionId);
        NearbyStoreProductVO vo = new NearbyStoreProductVO();
        vo.setGeoList(new ArrayList<>());
        vo.setProductList(new ArrayList<>());

        // 查询当前场次所有秒杀商品
        List<FlashPromotionProductBO> boList = flashPromotionMapper.selectAllNewestSecondPage(flashPromotionId, NumberUtils.LONG_ZERO, productType.byteValue(), mapQuery, false);
        if (BlankUtil.isEmpty(boList)) {
            return null;
        }
        // 构造Redis GEO列表
        loadFlashInfoToRedisGeo(fullKey, boList);

        // 从redis中计算出附近符合条件的列表
        if (BlankUtil.isEmpty(radius)) {
            radius = 500000d;
        }
        List<RedisGeoDTO> list = redisService.gRadius(fullKey, mapQuery.getLng(), mapQuery.getLat(), radius);
        if (BlankUtil.isNotEmpty(list)) {
            // 获取默认店铺
            RedisGeoDTO redisGeoDTO = list.get(0);
            if (BlankUtil.isNotEmpty(partnerId)) {
                redisGeoDTO = getGeoById(list, partnerId);
                list.remove(redisGeoDTO);
                list.add(0, redisGeoDTO);
            }
            Long id = redisGeoDTO.getId();
            Double distance = redisGeoDTO.getDistance();

            // 根据供应商ID获取信息
            PartnerUserDTO partnerUserDTO = partnerMapper.getPartnerInfoById(id);
            if (BlankUtil.isNotEmpty(partnerUserDTO)) {
                partnerUserDTO.setDistance(distance);
            }
            // 根据供应商ID获取当场上架的产品信息
            List<FlashPromotionProductBO> collect = boList.stream()
                                                          .filter(bo -> BlankUtil.isNotEmpty(bo.getPartnerId()))
                                                          .filter(bo -> bo.getPartnerId().equals(id))
                                                          .collect(Collectors.toList());
            // 封装结果
            vo.setPartnerInfo(partnerUserDTO);
            vo.setProductList(collect);
            vo.setGeoList(list);
        }
        return vo;
    }

    private RedisGeoDTO getGeoById(List<RedisGeoDTO> list, Long id) {
        for (RedisGeoDTO redisGeoDTO : list) {
            if (redisGeoDTO.getId().equals(id)) {
                return redisGeoDTO;
            }
        }
        return new RedisGeoDTO();
    }

    /**
     * 加载秒杀信息到Redis中
     *
     * @param fullKey key
     * @param boList  列表数据
     */
    private void loadFlashInfoToRedisGeo(String fullKey, List<FlashPromotionProductBO> boList) {
        Map<Object, Point> geoMap = new HashMap<>(100);
        for (FlashPromotionProductBO bo : boList) {
            Double lat = bo.getLat();
            Double lng = bo.getLng();
            if (BlankUtil.isNotEmpty(lat) && BlankUtil.isNotEmpty(lng)) {
                Point point = new Point(lng, lat);
                geoMap.put(bo.getPartnerId(), point);
            }
        }
        // 将经纬度数据加载到Redis中，并设置过期时间
        redisService.gAdd(fullKey, geoMap);
        redisService.expire(fullKey, redisConfig.getExpire().getMinute());
    }

}
