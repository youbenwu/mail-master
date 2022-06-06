package com.ys.mail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.*;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.OmsOrderMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.OmsOrderDTO;
import com.ys.mail.model.dto.OrderInfoDTO;
import com.ys.mail.model.dto.QuickOrderDTO;
import com.ys.mail.model.param.GiftOrderParam;
import com.ys.mail.model.query.QuickOrderQuery;
import com.ys.mail.model.vo.ElectronicVo;
import com.ys.mail.model.vo.OmsOrderVO;
import com.ys.mail.model.vo.PartnerTodayResultsVO;
import com.ys.mail.model.vo.UserOrderVO;
import com.ys.mail.service.*;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdGenerator;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OmsOrderServiceImpl.class);

    @Autowired
    private OmsOrderMapper omsOrderMapper;
    @Autowired
    private OmsOrderItemService omsOrderItemService;
    @Autowired
    private UmsRealNameService umsRealNameService;
    @Autowired
    private UmsUserInviteRuleService umsUserInviteRuleService;
    @Autowired
    private PmsProductService productService;
    @Autowired
    private UmsUserService umsUserService;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private UmsIncomeService incomeService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private SysSettingService sysSettingService;
    @Autowired
    private UmsUserInviteService umsUserInviteService;

    @Override
    public List<OmsOrderVO> getOrderList(int orderStatus, Long orderId, Boolean cpyType, String productName) {
        OmsOrderDTO omsOrderDTO = new OmsOrderDTO();
        omsOrderDTO.setUserId(UserUtil.getCurrentUser().getUserId());
        omsOrderDTO.setOrderStatus(orderStatus);
        omsOrderDTO.setOrderId(orderId);
        omsOrderDTO.setCpyType(cpyType);
        omsOrderDTO.setProductName(productName);
        //获取订单数据
        return omsOrderMapper.selectOrderList(omsOrderDTO);
    }

    @Override
    public List<UserOrderVO> getUserOrderList(String userId, String orderId, Integer pageSize) {
        // 获取parentId
        UmsUserInvite invite = umsUserInviteService.getOne(new SqlLambdaQueryWrapper<UmsUserInvite>().eq(UmsUserInvite::getUserId, userId));
        // 初始化变量
        List<UserOrderVO> inviteOrderData = new ArrayList<>();
        if (BlankUtil.isNotEmpty(invite)) {
            Long parentId = invite.getParentId();
            inviteOrderData = omsOrderMapper.getUserOrderList(Long.valueOf(userId), parentId, true, orderId, pageSize);
        }
        return inviteOrderData;
    }

    @Override
    public boolean updateOrder(OmsOrder build) {
        return omsOrderMapper.updateOrder(build);
    }

    @Override
    public boolean updateByCpyType(String orderSn, Byte cpyType) {
        return omsOrderMapper.updateByCpyType(orderSn, cpyType);
    }

    @Override
    public OrderInfoDTO getOrderInfo(String orderSn) {
        return omsOrderMapper.selectOrderInfo(orderSn);
    }

    /**
     * 生成高级会员支付(礼品)订单
     *
     * @return 订单SN，价格
     */
    @Override
    public CommonResult<GenerateOrderBO> generateGiftOrder(String userImageString, String cpyType) {
        // 判断该人脸数据是否已被有效注册，如果有则不能再次生成订单
        Boolean openFaceIdentify = sysSettingService.getSettingValue(SettingTypeEnum.twenty_nine);
        if (BlankUtil.isNotEmpty(openFaceIdentify) && openFaceIdentify && BlankUtil.isNotEmpty(userImageString)) {
            CommonResult<Boolean> commonResult = umsUserService.verifyFace(userImageString);
            ApiAssert.noValue(commonResult.getData(), BusinessErrorCode.USER_IMAGE_STRING_EXIST);
        }

        // 验证当前用户是否支付99元，如果是则无需再支付，否则进入下一步
        UmsUser currentUser = UserUtil.getCurrentUser();
        ApiAssert.isTrue(Objects.equals(NumberUtils.INTEGER_ONE, currentUser.getPaymentType()), CommonResultCode.ERR_REPAYMENT_CODE);

        // 读取设置中的付费会员价格，需要除以100
        Integer price = sysSettingService.getSettingValue(SettingTypeEnum.fourteen);
        Long amount = Long.valueOf(price);
        OmsOrder giftOrder = this.getGiftOrder(currentUser.getUserId());
        // 验证该用户是否已经存在礼品订单，如果有则返回该订单，否则进入下一步
        if (BeanUtil.isNotEmpty(giftOrder)) {
            amount = giftOrder.getPayAmount();
            return CommonResult.success(new GenerateOrderBO(giftOrder.getOrderSn(), Long.toString(amount)));
        }

        // 3、生成礼品订单（部分数据暂时为空，该订单状态为待付款、假设实付金额为99、订单类型为3等）
        OmsOrder omsOrder = OmsOrder.builder().orderId(IdWorker.generateId()).userId(currentUser.getUserId())
                                    .orderSn(IdGenerator.INSTANCE.generateId()).totalAmount(NumberUtils.LONG_ZERO)
                                    .payAmount(amount).payType(NumberUtils.INTEGER_ZERO)
                                    .sourceType(NumberUtils.INTEGER_ONE).orderStatus(NumberUtils.INTEGER_ZERO)
                                    .orderType(3).receiverName("").receiverPhone("").receiverProvince("")
                                    .receiverCity("").receiverRegion("").receiverAddress("")
                                    .cpyType(BlankUtil.isNotEmpty(cpyType) ? Byte.valueOf(cpyType) : NumberUtils.BYTE_ZERO)
                                    .build();
        // 更新订单
        boolean flag = this.saveOrUpdate(omsOrder);

        // TODO 更新用户人脸信息到用户表中的userImageString字段（如果有定时关闭订单操作，应该在那里清空，暂时在此处清空）
        if (flag) {
            // 清空其他无效人脸数据，压缩数据
            userImageString = DigestUtils.sha512Hex(userImageString);
            LambdaUpdateWrapper<UmsUser> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(UmsUser::getUserImageString, StringUtils.EMPTY)
                   .eq(UmsUser::getUserImageString, userImageString);
            umsUserService.update(wrapper);
            // 更新人脸数据到当前用户中
            currentUser.setUserImageString(userImageString);
            if (!umsUserService.updateById(currentUser)) {
                throw new ApiException(BusinessErrorCode.ORDER_GIFT_FAILED);
            }
            // 清空缓存
            userCacheService.delUser(currentUser.getPhone());
        }

        return flag ? CommonResult.success(new GenerateOrderBO(omsOrder.getOrderSn(), Long.toString(amount))) : CommonResult.failed(BusinessErrorCode.ORDER_STOCK_FAILED);
    }

    /**
     * 领取礼品下单接口
     *
     * @param param 下单参数
     * @return bool
     */
    @Override
    public CommonResult<Boolean> updateGiftOrder(GiftOrderParam param) {
        // 验证是否已付款、是否高级用户、是否已领取
        UmsUser currentUser = UserUtil.getCurrentUser();

        if (!Objects.equals(currentUser.getPaymentType(), NumberUtils.INTEGER_ONE)) {
            return CommonResult.failed(CommonResultCode.ERR_USER_NON_PAYMENT);
        }
        if (!Objects.equals(currentUser.getRoleId(), NumberUtils.INTEGER_ONE)) {
            return CommonResult.failed(CommonResultCode.ERR_USER_NOT_SENIOR_ROLE);
        }
        // TODO：新需求要移除实名验证，改为人脸数据验证（判断是否已注册）
        Boolean openFaceIdentify = sysSettingService.getSettingValue(SettingTypeEnum.twenty_nine);
        if (BlankUtil.isNotEmpty(openFaceIdentify) && openFaceIdentify) {
            ApiAssert.noValue(currentUser.getUserImageString(), BusinessErrorCode.USER_IMAGE_STRING_UNREGISTERED);
        }

        // 验证是否已经兑换礼品
        if (currentUser.getExchangeGift()) {
            return CommonResult.failed(BusinessErrorCode.ERR_PRODUCT_RE_EXCHANGE);
        }

        // 验证是否存在订单，并获取之前生成的空订单ID
        OmsOrder giftOrder = this.getGiftOrder(currentUser.getUserId());
        if (BeanUtil.isEmpty(giftOrder)) {
            return CommonResult.failed(BusinessErrorCode.ORDER_NOT_EXIST);
        }

        // 验证该商品ID是否是礼品列表中有的，（不能兑换没有的礼品）
        List<PmsProduct> giftList = productService.getGift();
        // 用户领取的礼品ID
        String productId = param.getProductId();
        // 是否在礼品列表
        boolean isExist = false;
        // 当前操作的礼品
        PmsProduct pmsProduct = new PmsProduct();
        for (PmsProduct product : giftList) {
            if (String.valueOf(product.getProductId()).equals(productId)) {
                isExist = true;
                pmsProduct = product;
                break;
            }
        }
        if (!isExist) {
            return CommonResult.failed(BusinessErrorCode.GIFT_NOT_EXIST);
        }

        // 开始生成订单详情，并关联到之前的订单中
        OmsOrderItem omsOrderItem = OmsOrderItem.builder().orderItemId(IdWorker.generateId())
                                                .orderId(giftOrder.getOrderId()).productId(Long.valueOf(productId))
                                                .pdtCgyId(Long.valueOf(param.getPdtCgyId()))
                                                .orderSn(giftOrder.getOrderSn()).productPic(pmsProduct.getPic())
                                                .productName(param.getProductName()).productPrice(NumberUtils.LONG_ZERO)
                                                .productQuantity(NumberUtils.INTEGER_ONE).productAttr("[]").build();
        if (!omsOrderItemService.save(omsOrderItem)) {
            throw new ApiException(BusinessErrorCode.ORDER_STOCK_FAILED);
        }

        // 更新订单状态为待发货，以及其他地址信息、用户表中的领取状态等
        BeanUtils.copyProperties(param, giftOrder);
        giftOrder.setOrderStatus(NumberUtils.INTEGER_ONE);
        if (!this.updateById(giftOrder)) {
            throw new ApiException(BusinessErrorCode.ORDER_UPDATE_FAILED);
        }
        currentUser.setExchangeGift(Boolean.TRUE);
        if (!umsUserService.updateById(currentUser)) {
            throw new ApiException(BusinessErrorCode.ORDER_GIFT_FAILED);
        }

        // 清空用户缓存
        userCacheService.delUser(currentUser.getPhone());

        // 领取成功返回
        return CommonResult.success("兑换礼品成功", Boolean.TRUE);
    }

    /**
     * 获取高级会员支付(礼品)订单
     *
     * @param userId 用户ID
     * @return 订单对象
     */
    @Override
    public OmsOrder getGiftOrder(Long userId) {
        QueryWrapper<OmsOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_type", 3).eq("delete_status", 0).eq("user_id", userId).orderByDesc("order_id")
               .last("LIMIT 1");
        return this.getOne(wrapper);
    }

    /**
     * 回调使用：高级会员支付成功后更新用户、订单等信息
     *
     * @param userId      操作的用户ID
     * @param order       系统订单对象
     * @param totalAmount 支付金额，需要乘以100后调用
     * @return bool
     */
    @Override
    public boolean updateUserForSeniorPay(Long userId, OmsOrder order, Long totalAmount) {
        UmsUser user = umsUserService.getById(userId);
        if (BeanUtil.isNotEmpty(user)) {
            // 校验价格，需要与订单中的金额保持一致，否则为非法操作
            ApiAssert.isFalse(order.getPayAmount().equals(totalAmount), CommonResultCode.ILLEGAL_REQUEST);
            // 更新用户付款字段  0 1 0
            user.setPaymentType(NumberUtils.INTEGER_ONE);
            boolean flag = umsUserService.updateById(user);
            LOGGER.info("【升级高级会员付款回调】,更新用户：{}", flag);
            if (!flag) throw new ApiException("更新失败");
            // 删除该用户的缓存
            userCacheService.delUser(user.getPhone());
            return true;
        }
        return false;
    }

    @Override
    public void updateByOrderType(Long orderId) {
        // , Integer orderStatusOne, Integer orderStatusSeven
        omsOrderMapper.updateByOrderType(orderId);
    }

    @Override
    public List<QuickOrderDTO> getAllQuickOrder(QuickOrderQuery query) {

        return omsOrderMapper.selectAllQuickOrder(query, UserUtil.getCurrentUser().getUserId());
    }

    @Override
    public PartnerTodayResultsVO partnerTodayResults(Long partnerId) {
        return omsOrderMapper.selectTodayScore(partnerId);
    }

    @Override
    public Page<ElectronicVo> selectElectronic(Query query, Long partnerId) {
        Page<ElectronicVo> page = new Page<>(query.getPageNum(), query.getPageSize());
        return omsOrderMapper.selectElectronic(page, partnerId);
    }

    @Override
    public List<QuickOrderDTO> getAllMakerOrder(QuickOrderQuery query) {
        return omsOrderMapper.selectAllMakerOrder(query, UserUtil.getCurrentUser().getUserId());
    }

    @Override
    public OrderInfoDTO getNewOrderInfo(String orderSn) {
        OrderInfoDTO orderInfoDTO = omsOrderMapper.selectOrderInfo(orderSn);
        orderInfoDTO.getOmsOrderItem().stream().filter(Objects::nonNull).forEach(item -> {
            if (BlankUtil.isNotEmpty(item) && BlankUtil.isNotEmpty(item.getProductPrice()) && BlankUtil.isNotEmpty(item.getProductQuantity())) {
                item.setProductPrice(item.getProductPrice() / item.getProductQuantity());
            }
        });
        return orderInfoDTO;
    }
}
