package com.ys.mail.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbitmq.client.Channel;
import com.ys.mail.config.RabbitMqSmsConfig;
import com.ys.mail.entity.*;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.mapper.*;
import com.ys.mail.model.dto.*;
import com.ys.mail.model.mq.MSOrderCheckDTO;
import com.ys.mail.service.*;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdGenerator;
import com.ys.mail.util.IdWorker;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liky
 * @date 2021/12/29 14:07
 * @description 秒杀队列
 */
@Component
public class RabbitMqMSListener {

    @Autowired
    private OmsOrderService orderService;
    @Autowired
    private OmsOrderItemService orderItemService;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private SmsFlashPromotionProductService smsFlashPromotionProductService;
    @Autowired
    private SmsFlashPromotionProductMapper flashPromotionProductMapper;
    @Autowired
    private UmsUserInviteMapper umsUserInviteMapper;
    @Autowired
    private UmsIncomeMapper umsIncomeMapper;
    @Autowired
    private OmsVerificationOrderMapper verificationOrderMapper;
    @Autowired
    private PmsVerificationCodeMapper verificationCodeMapper;
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private SysSettingService settingService;

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMqMSListener.class);

    // 下单mq 计算实时收益
    @RabbitListener(queues = RabbitMqSmsConfig.DLX_QUEUE_MS)
    @Transactional(rollbackFor = Exception.class)
    public void listenerQueue(Message msg, Channel channel, MSOrderCheckDTO dto) {
        LOGGER.info("=======实时收益计算 start=======");
        try {
            LOGGER.info("实时收益计算MQ参数:{}", JSON.toJSONString(dto));
            Long orderId = dto.getOrderId();
            OmsOrder order = orderService.getById(orderId);
            // 此处是判断订单状态未付款则关闭订单
            if (ObjectUtils.isNotEmpty(order) && order.getOrderStatus().equals(OmsOrder.OrderStatus.ZERO.key())) {
                // if(ObjectUtils.isNotEmpty(order)&&false){
                LOGGER.info("查询到未付款订单信息:{}", JSON.toJSONString(order));
//                if(ObjectUtils.isNotEmpty(order)&&false){
                // 关闭订单
                order.setOrderStatus(OmsOrder.OrderStatus.FOUR.key());
                // 恢复库存 todo:如果秒杀订单哪里 修改了 扣减了 pms_product 的库存 此处也需要修改成恢复pms_product库存
                flashPromotionProductMapper.restoreInventory(dto.getMap());
                // 恢复销量
                OmsOrderItem orderItem = orderItemService.getOneByOrderId(orderId);
                // 此次购买数量
                Integer productQuantity = orderItem.getProductQuantity();
                // 产品id
                Long productId = orderItem.getProductId();
                // 减销量
                productMapper.updateReduce(productId, productQuantity);
                LOGGER.info("恢复库存-销量信息");
            } else if (ObjectUtils.isNotEmpty(order) && BlankUtil.isNotEmpty(order.getTransId())) {
                // }else if(ObjectUtils.isNotEmpty(order)){
                LOGGER.info("查询到付款的订单信息:{}", JSON.toJSONString(order));
                Date createTime = order.getCreateTime();
                Integer yyyyMM = Integer.valueOf(new SimpleDateFormat("yyyyMM").format(createTime));
                // 结算
                List<SmsFlashPromotionProduct> productList = flashPromotionProductMapper.selectUserIncomeSQLQ(dto.getMap());
                for (int i = 0; i < productList.size(); ++i) {
                    UmsIncome umsIncome = new UmsIncome();                                      // 收益表数据

                    SmsFlashPromotionProduct product = productList.get(i);                     // {秒杀}
                    Long promotionPdtId = product.getFlashPromotionPdtId();       // 秒杀表.Id
                    Long publisherId = product.getPublisherId();               // 秒杀表.持有人
                    Long productId = product.getProductId();                 // 秒杀表.产品id
                    Integer flashPromotionCount = dto.getMap().get(promotionPdtId);       // 秒杀表.购买数量
                    Long flashPromotionPrice = product.getFlashPromotionOriginPrice(); // 秒杀表.秒杀金额

                    // 若有为Null  直接跳过
                    if (ObjectUtil.isNull(publisherId) || ObjectUtil.isNull(productId) ||
                            ObjectUtil.isNull(flashPromotionCount) || flashPromotionCount.equals(0) || ObjectUtil.isNull(flashPromotionPrice)) {
                        continue;
                    }
                    LOGGER.info("收益人id:{}", publisherId);
                    LOGGER.info("秒杀产品id:{}", promotionPdtId);
                    UmsIncome newest = umsIncomeMapper.selectNewestByUserId(publisherId);       // {收益}---------并发

                    Long todayIncome = newest != null ? newest.getTodayIncome() : 0L;                 // 收益表.(最新)今日收益
                    Long allIncome = newest != null ? newest.getAllIncome() : 0L;                   // 收益表.(最新)总收益
                    Long balance = newest != null ? newest.getBalance() : 0L;                     // 收益表.(最新)结余

                    PmsProduct pmsProduct = productMapper.selectById(productId);                // {产品}
                    String productName = pmsProduct != null ? pmsProduct.getProductName() : "id:" + productId;                           // 产品表.产品名

                    BigDecimal todayIncomeBg = new BigDecimal(todayIncome);              // Bg.(最新)今日收益
                    BigDecimal allIncomeBg = new BigDecimal(allIncome);                // Bg.(最新)总收益
                    BigDecimal balanceBg = new BigDecimal(balance);                  // Bg.(最新)结余
                    BigDecimal flashPromotionCountBg = new BigDecimal(flashPromotionCount);      // Bg.购买数量
                    BigDecimal flashPromotionPriceBg = new BigDecimal(flashPromotionPrice);      // Bg.原价

                    // es 用户佣金比
                    GetRequest getRequest = new GetRequest("game_record").id(publisherId.toString());
                    GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
                    String sourceAsString = response.getSourceAsString();

                    GameRecord gameRecord = new GameRecord();
                    if (StringUtils.isBlank(sourceAsString)) {
                        // topo: 0.005d 用户
                        gameRecord.setRatio(0.005D);
                    } else {
                        ESGameRecord parse = JSON.parseObject(sourceAsString, ESGameRecord.class);
                        List<GameRecord> collect = parse.getEarnings().stream()
                                                        .filter(obj -> obj.getDate().equals(yyyyMM))
                                                        .collect(Collectors.toList());
                        if (collect.size() == 0) {
                            gameRecord.setRatio(0.005D);
                        } else {
                            gameRecord = collect.get(0);
                        }
                    }

                    BigDecimal ratioBg = BigDecimal.valueOf(gameRecord.getRatio()).add(BigDecimal.valueOf(1));
                    LOGGER.info("受益人佣金比例:{}", ratioBg);
                    // 最终收益                               乘以数量                                计算价格
                    BigDecimal income = flashPromotionPriceBg.multiply(flashPromotionCountBg).multiply(ratioBg);
                    LOGGER.info("该产品购入原价:{},该产品购入订单{},受益人此次收益:{}", flashPromotionPrice, orderId, income);
                    // 此次计算今日收益
                    BigDecimal todayIncomeBgAddIncome = todayIncomeBg.add(income);
                    // 此次总收益
                    BigDecimal allIncomeBgAddIncome = allIncomeBg.add(income);
                    // 此次结余
                    BigDecimal balanceBgBgAddIncome = balanceBg.add(income);
                    // 收益计算
                    BigDecimal integral = income.subtract(flashPromotionPriceBg);

                    // 收益ID
                    umsIncome.setIncomeId(IdWorker.generateId());
                    // 收益人
                    umsIncome.setUserId(publisherId);
                    // 收益数
                    umsIncome.setIncome(income.longValue());
                    // 本金 2022-06-10
                    umsIncome.setOriginal(flashPromotionPriceBg.longValue());
                    // 本次纯收益，积分
                    umsIncome.setIntegral(integral.longValue());
                    // 今日收益
                    umsIncome.setTodayIncome(todayIncomeBgAddIncome.longValue());
                    // 总收益
                    umsIncome.setAllIncome(allIncomeBgAddIncome.longValue());
                    // 结余
                    umsIncome.setBalance(balanceBgBgAddIncome.longValue());
                    // 收益类型  1-秒杀收益
                    umsIncome.setIncomeType(1);
                    // 1->用户秒杀购买
                    umsIncome.setOriginType((byte) 1);

                    // 默认为2支付宝先写死
                    umsIncome.setPayType(3);
                    // 加入唯一商品主键id
                    umsIncome.setFlashPromotionPdtId(promotionPdtId);
                    // 来源
                    umsIncome.setDetailSource("用户秒杀收益-产品:(" + productName + ")");
                    // 备注 来源
                    umsIncome.setRemark("用户实时收益-用户:{" + order.getUserId() + "}秒杀-产品持有人:{" + publisherId + "}秒杀产品:{" + promotionPdtId + "},数量为:{" + flashPromotionCount + "}");

                    umsIncomeMapper.insert(umsIncome);
                }
            } else if (ObjectUtils.isNotEmpty(order)) {
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
                LOGGER.error("未查询到支付的订单信息id为:{}", orderId);
                throw new RuntimeException("MQ秒杀单5分钟-消费失败");
            } else {
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
                LOGGER.error("未查询到订单信息id为:{}", orderId);
                throw new RuntimeException("MQ秒杀单5分钟-消费失败");
            }
            /** 测试使用**/
            // confirmReceiptRatio(order.getUserId(),order.getCreateTime());
            /** 测试使用 **/
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LOGGER.info("=======实时收益计算 end=======");
        }
    }


    // 支付宝 发送的mq  计算是否是有效用户的
    @RabbitListener(queues = RabbitMqSmsConfig.CHANGE_ACTIVE_QUEUE)
    public void listenerChangeActive(Message msg, Channel channel, ChangeActiveDto dto) {
        try {
            String outTradeNo = dto.getOutTradeNo();
            OmsOrder order = orderService.getOne(new QueryWrapper<OmsOrder>().eq("order_sn", outTradeNo));
            if (BlankUtil.isNotEmpty(order.getPartnerId())) {
                Long orderId = order.getOrderId();
                // 商品数量
                Integer count = orderItemService.getQuantity(orderId);
                Long verificationId = IdWorker.generateId();

                // 插入订单核销表
                OmsVerificationOrder verificationOrder = new OmsVerificationOrder();
                verificationOrder.setVerificationOrderId(IdWorker.generateId());
                verificationOrder.setVerificationId(verificationId);
                verificationOrder.setOrderId(order.getOrderSn());
                verificationOrder.setQuantity(count);
                verificationOrderMapper.insert(verificationOrder);

                // 插入核销表
                PmsVerificationCode verificationCode = new PmsVerificationCode();
                verificationCode.setVerificationId(verificationId);
                verificationCode.setPartnerId(order.getPartnerId());
                verificationCode.setCode(IdGenerator.INSTANCE.cancelId());

                // 加入截止时间
                Date expireTime = smsFlashPromotionProductService.getExpireTime(orderId);
                verificationCode.setExpireTime(expireTime);

                verificationCodeMapper.insert(verificationCode);
            }
            // es 添加活跃日期
            confirmReceiptRatio(dto.getUserId(), dto.getDate());
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 给支付使用    确认收货 直接查询(ps:一定要走 支付流程 否则 确认收货可能为null)
    // 计算有效数
    public void confirmReceiptRatio(Long id, Date date) {
        String userId = String.valueOf(id);
        Double commission = 0.005D;
        try {
            // 获取id
            GetRequest getRequest = new GetRequest("game_record").id(userId);
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
            String sourceAsString = response.getSourceAsString();
            IndexRequest indexRequest = new IndexRequest("game_record");
            // 佣金比例
            if (StringUtils.isBlank(sourceAsString)) {
                //  若无直接插入一条 新的 年月日
                ESGameRecord esGameRecord = new ESGameRecord();

                GameRecord gameRecord = new GameRecord();
                gameRecord.setDate(Integer.valueOf(new SimpleDateFormat("yyyyMM").format(date)));
                gameRecord.setRatio(commission);
                esGameRecord.setEarnings(Collections.singletonList(gameRecord));
                esGameRecord.setGameDate(Collections.singletonList(Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(date))));

                indexRequest.id(userId).source(JSONUtil.toJsonStr(esGameRecord), XContentType.JSON);
                client.index(indexRequest, RequestOptions.DEFAULT);
            } else {
                ESGameRecord esGameRecord = JSON.parseObject(sourceAsString, ESGameRecord.class);
                List<Integer> gameDate = esGameRecord.getGameDate();
                // 插入 今天确认收货记录
                if (ObjectUtils.isEmpty(gameDate)) {
                    esGameRecord.setGameDate(Collections.singletonList(Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(date))));
                    indexRequest.id(userId).source(JSONUtil.toJsonStr(esGameRecord), XContentType.JSON);
                    client.index(indexRequest, RequestOptions.DEFAULT);
                } else {
                    Integer yyyyMMdd = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(date));
                    List<Integer> collect = gameDate.stream().filter(yyyyMMdd::equals).collect(Collectors.toList());
                    if (ObjectUtils.isEmpty(collect)) {
                        gameDate.add(yyyyMMdd);
                        indexRequest.id(userId).source(JSONUtil.toJsonStr(esGameRecord), XContentType.JSON);
                        client.index(indexRequest, RequestOptions.DEFAULT);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 插入团长信息
        List<UmsUserInvite> umsUserInvites = umsUserInviteMapper.selectList(Wrappers.<UmsUserInvite>lambdaQuery()
                                                                                    .eq(UmsUserInvite::getParentId, userId));
        if (ObjectUtils.isNotEmpty(umsUserInvites)) {
            groupMaster(userId, date);
        }
    }


    public void groupMaster(String userId, Date date) {
        Double commission = settingService.getSettingValue(SettingTypeEnum.four);
        commission = BlankUtil.isEmpty(commission) ? 0.002D : commission;
        commission = ObjectUtil.equal(commission, NumberUtils.DOUBLE_ZERO) ? 0.002D : commission;
        //Double commission = 0.003D;
        try {
            // 获取id
            GetRequest getRequest = new GetRequest("group_master").id(userId);
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
            String sourceAsString = response.getSourceAsString();
            IndexRequest indexRequest = new IndexRequest("group_master");
            // 佣金比例
            if (StringUtils.isBlank(sourceAsString)) {
                //  若无直接插入一条 新的 年月日
                ESGroupMaster esGroupMaster = new ESGroupMaster();
                GroupMater groupMater = new GroupMater();

                groupMater.setDate(Integer.valueOf(new SimpleDateFormat("yyyyMM").format(date)));
                groupMater.setRatio(commission);
                groupMater.setLevel(0);
                esGroupMaster.setEarnings(Collections.singletonList(groupMater));
                esGroupMaster.setGameDate(Collections.singletonList(Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(date))));

                indexRequest.id(userId).source(JSONUtil.toJsonStr(esGroupMaster), XContentType.JSON);
                client.index(indexRequest, RequestOptions.DEFAULT);
                return;
            }
            ESGroupMaster esGroupMaster = JSON.parseObject(sourceAsString, ESGroupMaster.class);
            List<Integer> gameDate = esGroupMaster.getGameDate();
            // 插入 今天确认收货记录
            if (ObjectUtils.isEmpty(gameDate)) {
                esGroupMaster.setGameDate(Collections.singletonList(Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(date))));
                indexRequest.id(userId).source(JSONUtil.toJsonStr(esGroupMaster), XContentType.JSON);
                client.index(indexRequest, RequestOptions.DEFAULT);
            } else {
                Integer yyyyMMdd = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(date));
                List<Integer> collect = gameDate.stream().filter(yyyyMMdd::equals).collect(Collectors.toList());
                if (ObjectUtils.isEmpty(collect)) {
                    gameDate.add(yyyyMMdd);
                    indexRequest.id(userId).source(JSONUtil.toJsonStr(esGroupMaster), XContentType.JSON);
                    client.index(indexRequest, RequestOptions.DEFAULT);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Transactional(rollbackFor = Exception.class)
//    @RabbitListener(queues = RabbitMqSmsConfig.CHANGE_ACTIVE_CART_QUEUE)
//    public void listenerCart(Message msg, Channel channel, String id) {
//        LOGGER.info("异步执行添加购物车基本信息开始----任务执行时间:{},线程名称:{}", LocalDateTime.now(),Thread.currentThread().getName());
//        try{
//        if(BlankUtil.isEmpty(id)){
//            LOGGER.info("消息丢失");
//            return;
//        }
//        OmsCartItem item=cartItemService.getByCartInfo(Long.valueOf(id));
//        if(BlankUtil.isEmpty(item)){
//            LOGGER.info("没有此购物车商品信息:{}",id);
//            return;
//        }
//        cartItemService.updateById(item);
//        channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
}
