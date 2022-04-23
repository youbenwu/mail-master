package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ys.mail.entity.*;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.SmsFlashPromotionMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.FlashPromotionProductBO;
import com.ys.mail.model.dto.FlashPromotionProductDTO;
import com.ys.mail.model.dto.SecondProductDTO;
import com.ys.mail.model.param.TimeShopParam;
import com.ys.mail.model.po.FlashPromotionProductPO;
import com.ys.mail.model.vo.FlashPromotionProductVO;
import com.ys.mail.service.*;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdGenerator;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-12 17:44
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SmsFlashPromotionServiceImpl extends ServiceImpl<SmsFlashPromotionMapper, SmsFlashPromotion> implements SmsFlashPromotionService {

    private static final Logger log = LoggerFactory.getLogger(SmsFlashPromotionServiceImpl.class);

    @Autowired
    private SmsFlashPromotionMapper flashPromotionMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PmsSkuStockService skuStockService;
    @Autowired
    private OmsOrderService orderService;
    @Autowired
    private OmsOrderItemService orderItemService;

    private static final Executor EXECUTOR = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("export-report-pool-thread-%d").setDaemon(false).setPriority(Thread.NORM_PRIORITY).build());

    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.homeAllSecondProduct}")
    private String redisKeyHomeAllSecondProduct;
    @Value("${redis.expire.homePage}")
    private Long redisExpireHomePage;

    @Override
    public List<FlashPromotionProductPO> getAllNewestSecond(Byte robBuyType) {
        // TODO,所有的商品加入到秒杀中,秒杀完用户删除redis中的,但是数据库中会增加一条数据进行下一轮key秒杀,
        // 那么这条秒杀就进不去了,那就有问题了,必须从数据库中才能刷新出来,这中间就有个bug了
        // 翻页id,场次id,场次id传0,第一次我给他全部查询出来
        return flashPromotionMapper.selectAllNewestSecond(robBuyType);
    }


    @Override
    public CommonResult<Boolean> saveTimeShop(TimeShopParam param) throws Exception{
        UmsUser currentUser = UserUtil.getCurrentUser();
        if(currentUser.getRoleId().equals(NumberUtils.INTEGER_ZERO)){
            return CommonResult.failed("不是高级用户", false);
        }
        // 对比价格
        PmsSkuStock skuStock = skuStockService.getById(param.getProductSkuId());
        long quantity = skuStock.getPromotionPrice() * param.getQuantity();
        if(!param.getProductPrice().equals(quantity)){
            return CommonResult.failed("金额不一致",false);
        }
        // TODO 限购数量要做判断,前端做判断,关联的商品与显示抢购表显示数量
        OmsOrder order = new OmsOrder();
        BeanUtils.copyProperties(param,order);
        Long userId = currentUser.getUserId();
        Long orderId = IdWorker.generateId();
        Long productPrice = param.getProductPrice();
        order.setUserId(userId);
        order.setOrderId(orderId);
        order.setPayAmount(quantity);
        order.setTotalAmount(productPrice);
        order.setPayAmount(productPrice);
        order.setOrderSn(IdGenerator.INSTANCE.generateId());
        order.setOrderType(NumberUtils.INTEGER_ONE);
        if(!orderService.save(order)){
            return CommonResult.failed("生成订单失败",false);
        }
        log.info("生成订单{}:",true);

        // 生成订单商品表
        OmsOrderItem orderItem = new OmsOrderItem();
        BeanUtils.copyProperties(param,orderItem);
        orderItem.setProductQuantity(param.getQuantity());
        orderItem.setOrderItemId(IdWorker.generateId());
        orderItem.setOrderId(orderId);
        if(!orderItemService.save(orderItem)){
            throw new ApiException("生成商品订单失败");
        }
        log.info("生成商品订单{}:",true);
        return CommonResult.success(true);
    }

    @Override
    public CommonResult<Boolean> savePostedOnline(String orderId, Long productPrice) {
        // 订单设置为失效,
        OmsOrder build = OmsOrder.builder()
                .orderId(Long.valueOf(orderId))
                .orderStatus(5)
                .build();
        if(!orderService.updateById(build)){
            return CommonResult.failed("修改订单失败");
        }
        // 轮回秒杀

        return null;
    }

    @Override
    public SecondProductDTO getSecondProduct(Integer ite,Byte cpyType) {
        return ite.equals(NumberUtils.INTEGER_ONE) ? flashPromotionMapper.selectSecondProduct(cpyType): null;
    }

    @Override
    public List<FlashPromotionProductBO> getAllNewestSecondPage(String flashPromotionId, String flashPromotionPdtId,Byte robBuyType) {
        return flashPromotionMapper.selectAllNewestSecondPage(Long.valueOf(flashPromotionId),Long.valueOf(flashPromotionPdtId),robBuyType);
    }

}
