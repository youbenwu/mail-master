package com.ys.mail.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.bo.UmsUserDetails;
import com.ys.mail.component.SmsClient;
import com.ys.mail.config.CfrConfig;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.constant.AlipayConstant;
import com.ys.mail.constant.FigureConstant;
import com.ys.mail.constant.NumberConstant;
import com.ys.mail.entity.PcReview;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.entity.UmsUserInvite;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.enums.SqlFormatEnum;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.UmsUserMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.alipay.AlipayPaidOutParam;
import com.ys.mail.model.alipay.BusinessParams;
import com.ys.mail.model.alipay.PayeeInfo;
import com.ys.mail.model.oauth.Auth;
import com.ys.mail.model.oauth.Base64Utils;
import com.ys.mail.model.oauth.DES;
import com.ys.mail.model.oauth.SignUtil;
import com.ys.mail.model.param.*;
import com.ys.mail.model.vo.ProductStoreVO;
import com.ys.mail.model.vo.UmsUserVo;
import com.ys.mail.model.vo.UserInviteDataVO;
import com.ys.mail.model.vo.UserInviteItemDataVO;
import com.ys.mail.security.util.JwtTokenUtil;
import com.ys.mail.service.*;
import com.ys.mail.util.*;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import com.ys.mail.wrapper.SqlQueryWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * app用户表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UmsUserServiceImpl extends ServiceImpl<UmsUserMapper, UmsUser> implements UmsUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsUserServiceImpl.class);

    /**
     * 万能验证码,给前端用
     */
    private static final String ALL_AUTH_CODE = "230122";

    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private SmsClient smsClient;
    @Autowired
    private UmsUserInviteService umsUserInviteService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private UmsIncomeService umsIncomeService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private SysSettingService sysSettingService;
    @Autowired
    private UmsUserBlacklistService blacklistService;
    @Autowired
    private CommonPayService commonPayService;
    @Autowired
    private UmsUserMapper umsUserMapper;
    @Autowired
    private IncomeService incomeService;
    @Autowired
    private SmsProductStoreService smsProductStoreService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${oauth.app_key}")
    private String oauthAppKey;
    @Value("${oauth.app_secret}")
    private String oauthAppSecret;
    @Value("${oauth.url}")
    private String oauthUrl;
    @Value("${oauth.app_store}")
    private String oauthAppStore;
    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.userVerify}")
    private String userVerify;
    @Value("${redis.key.bindAlipay}")
    private String bindAlipay;
    @Value("${redis.expire.common}")
    private Long expireCommon;
    @Value("${oauth.hulatu.app_key}")
    private String hulatuAppKey;
    @Value("${oauth.hulatu.app_secret}")
    private String hulatuAppSecret;
    @Value("${prop.staticAccessPath}")
    private String accessPath;

    @Override
    public UserDetails loadBySecurity(String phone) {
        UmsUser user = userCacheService.getUser(phone);
        if (BlankUtil.isEmpty(user)) {
            user = getOne(new QueryWrapper<UmsUser>().eq("phone", phone));
            Optional.ofNullable(user).orElseThrow(() -> new ApiException("没有此用户"));
            userCacheService.setUser(user);
        }
        return new UmsUserDetails(user);
    }

    @Override
    public CommonResult<String> getAuthCode(String phone, Byte type) {
        String code = RandomUtil.randomNumbers(NumberConstant.SIX);
        userCacheService.setAuthCode(phone, code);
        // TODO 发送手机号,暂时关闭到生产环境再打开
        smsClient.sendRegisterVerify(phone, code, type);
        LOGGER.info("发送验证码是:{}", code);
        return CommonResult.success(code);
    }

    @Override
    public CommonResult<String> userRegister(String phone, String authCode, Long parentId) {
        //先验证验证码码是否正确,设置一个万能的验证码,
        if (!authCode.equals(ALL_AUTH_CODE)) {
            if (!authCode.equals(userCacheService.getAuthCode(phone))) {
                return CommonResult.failed("验证码错误");
            }
        }
        if (BlankUtil.isNotEmpty(parentId)) {
            UmsUser user = getOne(new QueryWrapper<UmsUser>().eq("user_id", parentId));
            if (BlankUtil.isEmpty(user)) {
                return CommonResult.failed("二维码失效");
            }
        }
        String token = createToken(phone, parentId);
        return CommonResult.success(token);
    }

    /**
     * 返回生成的token
     *
     * @param phone
     * @return
     */
    private String createToken(String phone, Long parentId) {
        // 黑名单检测
        Boolean onBlackList = blacklistService.isOnBlackList(phone);
        if (onBlackList) {
            LOGGER.warn("【创建token拦截】- 黑名单手机号：{}", phone);
            throw new ApiException("请求失败");
        }

        //查询是否存在该用户,存在就直接登录,不存在就直接注册
        UmsUser user = getOne(new QueryWrapper<UmsUser>().eq("phone", phone));
        if (BlankUtil.isEmpty(user)) {
            user = new UmsUser();
            user.setUserId(IdWorker.generateId());
            // 先用6位生成随机
            user.setNickname(NickNameUtil.getRandom(3));
            user.setPhone(phone);
            //save(user);
            if (!BlankUtil.isEmpty(parentId)) {
                UmsUserInvite entity = new UmsUserInvite();
                entity.setUserInviteId(NumberUtils.LONG_ZERO);
                user.setRoleId(1);
                entity.setUserId(user.getUserId());
                entity.setParentId(parentId);
                umsUserInviteService.addUserInvite(entity);
            }
            save(user);
        } else {
            if (!BlankUtil.isEmpty(parentId)) {
                UmsUserInvite entity = new UmsUserInvite();
                entity.setUserInviteId(NumberUtils.LONG_ZERO);
                user.setRoleId(1);
                entity.setUserId(user.getUserId());
                entity.setParentId(parentId);
                umsUserInviteService.addUserInvite(entity);
                updateById(user);
            }
        }

        UmsUserDetails userDetails = new UmsUserDetails(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateToken(userDetails);
        return tokenHead + token;
    }

    @Override
    public CommonResult<String> userOauth(OauthParam param) throws Exception {
        // TODO 一键登录需要接口方提供app标识和app秘钥
        HashMap<String, Object> request = new HashMap<>(16);
        request.put("appkey", oauthAppKey);
        request.put("token", param.getToken());
        request.put("opToken", param.getOpToken());
        request.put("operator", param.getOperator());
        request.put("timestamp", System.currentTimeMillis());
        if (param.getPhoneType().equals(NumberUtils.INTEGER_ONE)) {
            request.put("md5", oauthAppStore);
        }
        request.put("sign", SignUtil.getSign(request, oauthAppSecret));
        String response = Auth.postRequestNoSecurity(oauthUrl, null, request);

        //把String转成json
        JSONObject result = JSONObject.parseObject(response);
        final String status = "status";
        if (CommonResultCode.SUCCESS.getCode() == result.getInteger(status)) {
            String res = result.getString("res");
            byte[] decode = DES.decode(Base64Utils.decode(res.getBytes()), oauthAppSecret.getBytes());
            JSONObject json = JSONObject.parseObject(new String(decode));
            // result.put("res",json); 不用返回给前端,只返回一个token就好了
            return CommonResult.success(createToken(json.get("phone").toString(), null));
        }
        LOGGER.error("失败消息:{}", result.get("error"));
        return CommonResult.failed("一键登录失败");
    }

    @Override
    public CommonResult<String> hulatuUserOauth(OauthParam param) throws Exception {
        // TODO 一键登录需要接口方提供app标识和app秘钥
        HashMap<String, Object> request = new HashMap<>(16);
        request.put("appkey", hulatuAppKey);
        request.put("token", param.getToken());
        request.put("opToken", param.getOpToken());
        request.put("operator", param.getOperator());
        request.put("timestamp", System.currentTimeMillis());
        if (param.getPhoneType().equals(NumberUtils.INTEGER_ONE)) {
            request.put("md5", oauthAppStore);
        }
        request.put("sign", SignUtil.getSign(request, hulatuAppSecret));
        String response = Auth.postRequestNoSecurity(oauthUrl, null, request);

        //把String转成json
        JSONObject result = JSONObject.parseObject(response);
        final String status = "status";
        if (CommonResultCode.SUCCESS.getCode() == result.getInteger(status)) {
            String res = result.getString("res");
            byte[] decode = DES.decode(Base64Utils.decode(res.getBytes()), hulatuAppSecret.getBytes());
            JSONObject json = JSONObject.parseObject(new String(decode));
            // result.put("res",json); 不用返回给前端,只返回一个token就好了
            return CommonResult.success(createToken(json.get("phone").toString(), null));
        }
        LOGGER.error("失败消息:{}", result.get("error"));
        return CommonResult.failed("一键登录失败");
    }

    @Override
    public CommonResult<Boolean> verifyPhone(String phone, String authCode) {
        //先验证验证码码是否正确
        String code = userCacheService.getAuthCode(phone);
        if (!authCode.equals(code)) {
            return CommonResult.failed("验证码错误");
        }
        return CommonResult.success(true);
    }

    @Override
    public CommonResult<Boolean> changePhone(String phone) {
        // 删除redis中的缓存
        UmsUser user = getOne(new QueryWrapper<UmsUser>().eq("phone", phone));
        if (!BlankUtil.isEmpty(user)) {
            return CommonResult.failed("已存在此手机号", false);
        }
        UmsUser currentUser = UserUtil.getCurrentUser();
        userCacheService.delUser(currentUser.getPhone());
        currentUser.setPhone(phone);
        boolean b = updateById(currentUser);
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @Override
    public CommonResult<Boolean> changeNickname(String nickname) {
        // 删除redis中的缓存
        UmsUser currentUser = UserUtil.getCurrentUser();
        userCacheService.delUser(currentUser.getPhone());
        currentUser.setNickname(nickname);
        boolean b = updateById(currentUser);
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @Override
    public CommonResult<Boolean> changeHeadPortrait(String headPortrait) {
        // 删除redis中的缓存
        UmsUser currentUser = UserUtil.getCurrentUser();
        userCacheService.delUser(currentUser.getPhone());
        currentUser.setHeadPortrait(headPortrait);
        boolean b = updateById(currentUser);
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @Override
    public CommonResult<String> securityVerifyPhone(String phone, String authCode) {
        //先验证验证码码是否正确
        String code = userCacheService.getAuthCode(phone);
        if (!authCode.equals(code)) {
            return CommonResult.failed("验证码错误");
        }
        //生成随机码
        String randomCode = IdWorker.generateId() + "";
        redisService.set(redisDatabase + ":" + userVerify + ":" + phone, randomCode, 180);
        return CommonResult.success(randomCode);
    }

    @Override
    public CommonResult<Boolean> bindAlipay(BindAlipayParam param) {
        // TODO 暂时以数据库中查询为准 判断验证码是否正确,支付宝姓名可以重复,账号不能重复
        UmsUser currentUser = UserUtil.getCurrentUser();
        if (!param.getAuthCode().equals(userCacheService.getAuthCode(currentUser.getPhone()))) {
            return CommonResult.failed(CommonResultCode.ERR_AUTH_CODE);
        }
        String alipayName = param.getAlipayName();
        String alipayAccount = param.getAlipayAccount();

        if (!alipayAccount.equals(currentUser.getAlipayAccount())) {
            QueryWrapper<UmsUser> eq = new QueryWrapper<UmsUser>().eq("alipay_account", alipayAccount);
            List<UmsUser> list = list(eq);
            if (!BlankUtil.isEmpty(list)) return CommonResult.failed(BusinessErrorCode.ERR_NAME_ACCOUNT_ALIPAY);
        }

        String key = redisDatabase + ":" + bindAlipay + ":" + currentUser.getUserId();
        if (!BlankUtil.isEmpty(redisService.get(key))) {
            return CommonResult.failed(BusinessErrorCode.ERR_BIND_ALIPAY);
        }

        // 存入一天
        redisService.set(key, alipayName + alipayAccount, expireCommon);

        UmsUser build = UmsUser.builder().alipayAccount(alipayAccount).alipayName(alipayName)
                               .userId(currentUser.getUserId()).build();
        boolean b = updateById(build);
        if (b) {
            userCacheService.delUser(currentUser.getPhone());
        }
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }

    /**
     * 支付宝秒提
     *
     * @param param 实体对象
     * @return 提现结果
     * @throws AlipayApiException 支付异常
     */
    @Override
    public CommonResult<String> depositAlipay(DepositAlipayParam param) throws AlipayApiException {
        // 实时读取系统设置
        Double temp = sysSettingService.getSettingValue(SettingTypeEnum.eight);
        Long maxSingleLimit = DecimalUtil.strToLongForMultiply(temp);// 单笔最大限额，读取出来需要乘以100
        Integer maxExCount = sysSettingService.getSettingValue(SettingTypeEnum.nine);// 每日最大提现次数
        temp = sysSettingService.getSettingValue(SettingTypeEnum.ten);
        Long maxSecondsEx = DecimalUtil.strToLongForMultiply(temp);// 单次最大秒提阈值，读取出来需要乘以100
        Boolean openWithdraw = sysSettingService.getSettingValue(SettingTypeEnum.eleven);// 是否开启APP端提现，默认为false
        // 暂停提现
        if (!openWithdraw) return CommonResult.failed("系统维护中，请稍后重试");
        // 日志模板
        List<String> template = new ArrayList<>();
        template.add("【提现日志】==> [ID：{},昵称：{}]的用户发起提现，预计提现金额为：[{}]元");
        template.add("【提现日志】==> [ID：{},昵称：{}]的用户发起提现，提现失败原因：{}");
        template.add("【提现日志】==> [ID：{},昵称：{}]的用户发起提现，{}");
        template.add("【提现日志】==> [ID：{},昵称：{}]的用户发起提现，预提现金额为：[{}]元，由于金额小于[{}]元无需审核，直接提现");
        // 时间定义
        String today = DateTool.getTodayNow();
        // 提取公共变量
        UmsUser currentUser = UserUtil.getCurrentUser();
        Long userId = currentUser.getUserId();
        String nickname = currentUser.getNickname();
        String transAmount = param.getTransAmount();
        LOGGER.info(template.get(0), userId, nickname, transAmount);
        // 校验支付密码与支付名称
        if (BlankUtil.isEmpty(currentUser.getPayPassword())) { // 校验是否设置密码
            return CommonResult.failed(CommonResultCode.ERR_TEM_PAY_CODE);
        } else if (!passwordEncoder.matches(param.getPayPassword(), currentUser.getPayPassword())) { // 校验输入密码
            return CommonResult.failed(CommonResultCode.ERR_NOT_PAY_CODE);
        }
        // 检查个人当天的审核次数
        PcReview review = reviewService.getNewestRecord(userId, today);
        if (BlankUtil.isNotEmpty(review) && Objects.equals(review.getReviewState(), PcReview.ReviewState.ZERO.key())) {
            return CommonResult.failed(BusinessErrorCode.ERR_DEPOSIT_MONEY_REVIEWING);
        }
        // 检查个人当天的提现次数
        if (umsIncomeService.getTodayCount(userId, UmsIncome.IncomeType.TWO.key(), today) >= maxExCount) {
            String message = BusinessErrorCode.ERR_DEPOSIT_MONEY_EX_COUNT.getMessage();
            message = String.format(message, maxExCount);
            LOGGER.info(template.get(1), userId, nickname, message);
            return CommonResult.failed(BusinessErrorCode.ERR_DEPOSIT_MONEY_EX_COUNT, message);
        }
        // 校验最小金额与余额是否足够,查询最新的账户收益（有则且次数且大于设置的次数时则不能提现）
        UmsIncome income = umsIncomeService.selectNewestByUserId(userId);
        Long balance = income.getBalance(); // 当前余额 5000 00
        Long money = DecimalUtil.strToLongForMultiply(transAmount); // 提现金额，乘以100
        if (BlankUtil.isEmpty(money)) return CommonResult.failed("提现异常");
        if (money < FigureConstant.DOUBLE_TEN) { // 提现金额不能小于0.1
            return CommonResult.failed(BusinessErrorCode.ERR_DEPOSIT_MONEY_LENGTH);
        } else if (BlankUtil.isEmpty(income) || balance < money) { // 余额不足
            return CommonResult.failed(CommonResultCode.ERR_USER_DEPOSIT);
        } else if (BlankUtil.isNotEmpty(maxSingleLimit) && money > maxSingleLimit) { // 单笔最大限额
            String message = BusinessErrorCode.REVIEW_DEPOSIT_MONEY_MAX.getMessage();
            message = String.format(message, DecimalUtil.longToStrForDivider(maxSingleLimit));
            return CommonResult.failed(BusinessErrorCode.REVIEW_DEPOSIT_MONEY_MAX, message);
        }
        // 提现大于该值，则需要审核
        if (BlankUtil.isNotEmpty(maxSecondsEx) && money > maxSecondsEx) {
            return reviewAmount(review, income, money);
        }
        return transferAccounts(income, money);
    }

    /**
     * 小额提现（直接转账）
     *
     * @param income 流水记录
     * @param money  提现金额，已经乘以100
     * @return 结果
     * @throws AlipayApiException e
     */
    private CommonResult<String> transferAccounts(UmsIncome income, Long money) throws AlipayApiException {
        boolean updateResult = false;
        UmsUser currentUser = UserUtil.getCurrentUser();
        String alipayName = currentUser.getAlipayName();
        Long userId = currentUser.getUserId();
        JSON depositTimeRange = sysSettingService.getSettingValue(SettingTypeEnum.nineteen);// 提现的时间范围
        if (BlankUtil.isNotEmpty(depositTimeRange)) {
            // 解析时间规则
            JSONObject jsonObject = JSONObject.parseObject(depositTimeRange.toString());
            String leftTimeText = String.valueOf(jsonObject.get("min"));
            String rightTimeText = String.valueOf(jsonObject.get("max"));
            boolean isBetween = DateTool.localTimeIsBetween(leftTimeText, rightTimeText);
            if (!isBetween) return CommonResult.failed(BusinessErrorCode.REVIEW_MIN_EX_TIME_OVERSIZE);
        }
        // 扣除手续费
        Map<String, Long> resultMap = incomeService.deductCharges(income, money, userId);
        Long newBalance = resultMap.get("balance");
        long balance = income.getBalance();
        Long newMoney = resultMap.get("money");
        long finalBalance = balance - money;
        if (BlankUtil.isNotEmpty(newBalance) && BlankUtil.isNotEmpty(newMoney)) {
            money = newMoney;
            finalBalance = newBalance - money;
        }
        String transAmount = DecimalUtil.longToStrForDivider(money);

        // 构建支付对象
        String orderSn = IdGenerator.INSTANCE.generateId();
        AlipayPaidOutParam build = AlipayPaidOutParam.builder().outBizNo(orderSn).transAmount(transAmount)
                                                     .productCode(AlipayConstant.PRODUCT_CODE)
                                                     .bizScene(AlipayConstant.BIZ_SCENE)
                                                     .orderTitle(AlipayConstant.USER_DEPOSIT)
                                                     .payeeInfo(PayeeInfo.builder()
                                                                         .identityType(AlipayConstant.IDENTITY_TYPE_LOGON)
                                                                         .identity(currentUser.getAlipayAccount())
                                                                         .name(alipayName).build())
                                                     .remark(orderSn + alipayName + AlipayConstant.USER_DEPOSIT)
                                                     .businessParams(BusinessParams.builder()
                                                                                   .payerShowName(AlipayConstant.JH_KJ + AlipayConstant.USER_DEPOSIT)
                                                                                   .build()).build();

        // 返回响应
        AlipayFundTransUniTransferResponse response = commonPayService.paidOut(build);
        JSONObject result = JSONObject.parseObject(response.getBody());
        if (response.isSuccess()) {
            // 添加提现流水：微服务架构这里可以直接返回给前端,异步操作这段插入
            LOGGER.info("提现{}", result);
            UmsIncome umsIncome = UmsIncome.builder().incomeId(IdWorker.generateId()).userId(userId)
                                           .income(NumberUtils.LONG_ZERO)
                                           .expenditure(money).balance(finalBalance)
                                           .todayIncome(income.getTodayIncome()).allIncome(income.getAllIncome())
                                           .incomeType(UmsIncome.IncomeType.TWO.key()) // 2->余额提现
                                           .incomeNo(response.getOrderId())
                                           // .incomeNo("")
                                           .orderTradeNo(orderSn)
                                           .detailSource("提现到账成功:" + transAmount + "元")
                                           .payType(UmsIncome.PayType.TWO.key()) // 提现到支付宝
                                           .build();
            updateResult = umsIncomeService.save(umsIncome);
            if (!updateResult) throw new ApiException("添加流水记录失败");
        } else {
            LOGGER.info("提现异常日志：{}-{}", BusinessErrorCode.ERR_ALIPAY_DEPOSIT.getMessage(), response.getSubMsg());
        }
        return updateResult ? CommonResult.success("提现成功", String.valueOf(finalBalance)) :
                CommonResult.failed(BusinessErrorCode.ERR_ALIPAY_DEPOSIT, String.valueOf(finalBalance));
    }

    /**
     * 提现审核金额
     *
     * @param review 审核记录
     * @param income 流水记录
     * @param money  提现金额，已经乘以100
     * @return 结果
     */
    private CommonResult<String> reviewAmount(PcReview review, UmsIncome income, Long money) {
        String todayBeforeTime = sysSettingService.getSettingValue(SettingTypeEnum.seven);// 最晚提交申请审核时间 是否在21点前
        UmsUser currentUser = UserUtil.getCurrentUser();
        Long userId = currentUser.getUserId();
        boolean updateResult;
        // 存在当天审核记录（每天同时最多存在一条未审核）
        if (BlankUtil.isNotEmpty(review)) {
            // 提现小于等于审核金额，判断审核结果
            if (money <= review.getReviewMoney()) {
                switch (EnumTool.getEnum(PcReview.ReviewState.class, review.getReviewState())) {
                    case MINUS_ONE:// 已失效
                        return CommonResult.failed(BusinessErrorCode.REVIEW_EX_TIME_LOSE);
                    case ZERO:// 审核中
                        return CommonResult.failed(BusinessErrorCode.ERR_DEPOSIT_MONEY_REVIEWING);
                    case ONE:// 审核已通过，后台审核时已直接转账
                        return CommonResult.failed(BusinessErrorCode.REVIEW_EX_PASS);
                    case TWO:// 审核不通过
                        return CommonResult.failed(BusinessErrorCode.REVIEW_EX_REFUSED.getMessage() + "，原因是(" + review.getReviewDescribe() + ")");
                    case THREE:// 审核关闭，允许重新发起审核
                        break;
                    case FOUR:// 审核已被用户手动取消，当天无法再申请
                        return CommonResult.failed(BusinessErrorCode.REVIEW_EX_COUNT_OVERSIZE);
                    default:
                        return CommonResult.failed("该笔审核状态异常，请联系客服");
                }
            } else { // 金额大于审核金额，需要重新审核
                if (review.getReviewState() != 3) {
                    return CommonResult.failed(BusinessErrorCode.REVIEW_EX_COUNT_OVERSIZE);
                }
            }
        }

        // 判断截止申请时间点
        if (DateTool.localTimeIsAfter(todayBeforeTime))
            return CommonResult.failed(BusinessErrorCode.REVIEW_EX_TIME_OVERSIZE);

        // 扣除手续费
        Map<String, Long> resultMap = incomeService.deductCharges(income, money, userId);
        Long rateIncomeId = resultMap.get("rateIncomeId");
        Long newBalance = resultMap.get("balance");
        Long newMoney = resultMap.get("money");
        long balance = income.getBalance();
        long finalBalance = balance - money;
        if (BlankUtil.isNotEmpty(newBalance) && BlankUtil.isNotEmpty(newMoney)) {
            money = newMoney;
            finalBalance = newBalance - money;
        }
        String transAmount = DecimalUtil.longToStrForDivider(money);
        // 从余额中冻结该笔提现金额，插入一条冻结流水记录
        UmsIncome umsIncome = UmsIncome.builder().incomeId(IdWorker.generateId()).userId(userId)
                                       .income(NumberUtils.LONG_ZERO).expenditure(money).balance(finalBalance)
                                       .todayIncome(income.getTodayIncome()).allIncome(income.getAllIncome())
                                       .incomeType(UmsIncome.IncomeType.FOUR.key()) // 4->审核资金
                                       .incomeNo("").orderTradeNo("").detailSource("系统审核中:" + transAmount + "元")
                                       .payType(UmsIncome.PayType.THREE.key()).build();
        updateResult = umsIncomeService.save(umsIncome);
        if (!updateResult) throw new ApiException("审核扣除资金失败");

        // 添加审核
        PcReview pcReview = PcReview.builder().reviewId(IdWorker.generateId()).userId(userId).reviewMoney(money)
                                    .rateIncomeId(rateIncomeId).alipayAccount(currentUser.getAlipayAccount())
                                    .alipayName(currentUser.getAlipayName()).build();
        updateResult = reviewService.save(pcReview);
        if (!updateResult) throw new ApiException("添加审核记录失败");
        return CommonResult.success(BusinessErrorCode.REVIEW_DEPOSIT_MONEY_EXCEED.getMessage(), String.valueOf(finalBalance));
    }

    @Override
    public UserDetails selectUserByRoleId(Integer roleId) {
        QueryWrapper<UmsUser> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId).orderByDesc("create_time").last("limit 1");
        return new UmsUserDetails(getOne(wrapper));
    }

    @Override
    public CommonResult<Boolean> setPaymentCode(UserTemPayCodeParam param) {
        // 判断当前随机码是否有效
        UmsUser currentUser = UserUtil.getCurrentUser();
        Long userId = currentUser.getUserId();
        String newPassword = param.getNewPassword();
        if (!newPassword.equals(param.getOldPassword())) {
            return CommonResult.failed(CommonResultCode.ERR_NOT_PAY_CODE);
        } else if (!verifyRandomCode(currentUser.getPhone(), param.getRandomCode())) {
            return CommonResult.failed(CommonResultCode.ERR_OPERATION_TIMEOUT);
        } else if (!BlankUtil.isEmpty(currentUser.getPayPassword())) {
            return CommonResult.failed(CommonResultCode.ERR_IS_PAY_CODE);
        }
        UmsUser user = new UmsUser(userId, passwordEncoder.encode(newPassword));
        boolean save = updateById(user);
        userCacheService.delUser(currentUser.getPhone());
        return save ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @Override
    public UmsUserVo info() {
        UmsUserVo vo = new UmsUserVo();
        UmsUser currentUser = UserUtil.getCurrentUser();
        currentUser.setPayPassword(BlankUtil.isEmpty(currentUser.getPayPassword()) ? FigureConstant.NOT_PAY_PASSWORD : FigureConstant.IS_PAY_PASSWORD);
        BeanUtils.copyProperties(currentUser, vo);
        ProductStoreVO productStoreVO = smsProductStoreService.getInfo();
        vo.setStoreInfo(productStoreVO);
        return vo;
    }

    @Override
    public CommonResult<Boolean> verifyFace(String userImageString) {
        QueryWrapper<UmsUser> wrapper = new QueryWrapper<>();
        if (BlankUtil.isEmpty(userImageString)) return CommonResult.failed("数据异常");
        userImageString = DigestUtils.sha512Hex(userImageString);
        wrapper.eq("payment_type", NumberUtils.INTEGER_ONE).eq("user_image_string", userImageString);
        int count = this.count(wrapper);
        if (count > 0) return CommonResult.failed(BusinessErrorCode.USER_IMAGE_STRING_EXIST);
        else
            return CommonResult.success(BusinessErrorCode.USER_IMAGE_STRING_UNREGISTERED.getMessage(), Boolean.TRUE); // 允许重新注册
    }

    /**
     * 判断当前随机码是否有效
     *
     * @param phone      用户电话号码
     * @param randomCode 随机码
     * @return
     */
    private boolean verifyRandomCode(String phone, String randomCode) {
        String key = redisDatabase + ":" + userVerify + ":" + phone;
        if (!redisService.hasKey(key)) {
            return false;
        }
        String code = (String) redisService.get(key);
        if (!randomCode.equals(code)) {
            return false;
        }
        redisService.del(key);
        return true;
    }

    @Override
    public CommonResult<Object> callVerifyFace(VerifyFaceParam param) {
        Map<String, Object> map = new HashMap<>();
        CfrConfig cfrConfig = new CfrConfig();
        map.put("wbappId", cfrConfig.getDaWeiHuAppId());
        map.put("userId", UserUtil.getCurrentUser().getUserId());
        map.put("version", "1.00");
        String url = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/access_token?" + "app_id=" + cfrConfig.getDaWeiHuAppId() + "&secret=" + cfrConfig.getDaWeiHuAppSecret() + "&grant_type=client_credential&version=1.00";
        HttpUtil.get(url);
        return null;
    }

    @Override
    public UserInviteDataVO getUserInviteDataList(String userId, Integer pageSize) {
        // 父级ID
        Long parentId = UserUtil.getCurrentUser().getUserId();
        // 获取key
        String fullKey = String.format("%s:%d", redisConfig.fullKey(redisConfig.getKey().getInviteUser()), parentId);
        // 初始化变量
        Map<String, Object> teamSumMap = new HashMap<>(1);
        Map<String, Object> cacheMap = new HashMap<>(2);
        List<UserInviteItemDataVO> pageInviteList;
        Integer memberOrderNum = NumberUtils.INTEGER_ZERO;
        Long memberOrderSum = NumberUtils.LONG_ZERO;
        int inviteTotal = 0;

        // 尝试从缓存中获取
        Map<Object, Object> o = redisService.hGetAll(fullKey);
        List<UserInviteItemDataVO> totalData = (List<UserInviteItemDataVO>) o.get("totalData");
        UserInviteDataVO inviteData = (UserInviteDataVO) o.get("inviteData");
        if (BlankUtil.isEmpty(totalData)) {
            // 统计团长的邀请总人数，如果没有则后面都跳过
            inviteTotal = umsUserInviteService.count(new SqlLambdaQueryWrapper<UmsUserInvite>().eq(UmsUserInvite::getParentId, parentId));
            // 统计其他信息
            if (inviteTotal > NumberUtils.INTEGER_ZERO) {
                // 统计团长分佣，当月
                teamSumMap = umsIncomeService.getMap(new SqlQueryWrapper<UmsIncome>().eq("user_id", parentId)
                                                                                     .eq("income_type", UmsIncome.IncomeType.SIX.key())
                                                                                     .compareDate(SqlFormatEnum.STRING_DATE_FORMAT_YM_EQ, "create_time", DateTool.getYearMonth())
                                                                                     .sum("income", "teamSum"));

                // 查询团长的所有下级信息(下级用户ID、被邀请人电话、被邀请人头像、被邀请时间) TODO：当迁移parentId到用户表后需要修改这里
                List<UserInviteItemDataVO> inviteInfoList = umsUserMapper.getUserInviteInfo(parentId);
                // 遍历出所有下级ID
                List<Long> inviteIds = inviteInfoList.stream().map(UserInviteItemDataVO::getUserId)
                                                     .collect(Collectors.toList());

                // 查询所有下级成员的有效消费记录（下级用户ID、有效消费金额、有效消费笔数），可以仅查询当月
                List<Map<String, Object>> inviteOrderList = umsUserMapper.getInviteOrderCollectByParent(parentId, inviteIds, true);

                // 统计：团长的所有下级有效订单总笔数、总金额
                if (BlankUtil.isNotEmpty(inviteOrderList)) {
                    memberOrderNum = inviteOrderList.stream().map(m -> Integer.valueOf(m.get("count").toString()))
                                                    .reduce(0, Integer::sum);
                    memberOrderSum = Long.valueOf(inviteOrderList.stream()
                                                                 .map(m -> Integer.valueOf(m.get("money").toString()))
                                                                 .reduce(0, Integer::sum));
                }

                // 合并下级数据
                totalData = mergeInviteData(inviteIds, inviteInfoList, inviteOrderList);
            }
        }

        // 计算分页
        if (BlankUtil.isNotEmpty(userId)) {
            pageInviteList = totalData.stream().filter(vo -> vo.getUserId() < Long.parseLong(userId)).limit(pageSize)
                                      .collect(Collectors.toList());
        } else {
            if (BlankUtil.isEmpty(totalData)) totalData = new ArrayList<>();
            pageInviteList = totalData.stream().limit(pageSize).collect(Collectors.toList());
        }

        // 封装结果
        if (BlankUtil.isEmpty(inviteData)) {
            Object teamSum = teamSumMap.get("teamSum");
            inviteData = UserInviteDataVO.builder().teamNum(inviteTotal)
                                         .teamSum(BlankUtil.isNotEmpty(teamSum) ? DecimalUtil.objToLong(teamSum) : NumberUtils.LONG_ZERO)
                                         .consumeNum(memberOrderNum).consumeTotal(memberOrderSum).build();
            // 计算缓存过期时间，隔天刷新
            Long expire = DateTool.getNowToNextDaySeconds();
            // 存入缓存
            cacheMap.put("totalData", totalData);
            cacheMap.put("inviteData", inviteData);
            redisService.hSetAll(fullKey, cacheMap, expire);
            // 不需要重复存入缓存
            inviteData.setInvite(pageInviteList);
        } else {
            // 更新分页数据
            inviteData.setInvite(pageInviteList);
        }
        return inviteData;
    }

    /**
     * 合并下级数据
     *
     * @param inviteInfoList  下级个人信息
     * @param inviteOrderList 下级有效订单数据
     * @return 合并完的数据
     */
    private List<UserInviteItemDataVO> mergeInviteData(List<Long> inviteIds, List<UserInviteItemDataVO> inviteInfoList, List<Map<String, Object>> inviteOrderList) {
        // 统计团长所有下级的邀请成员数（只会统计有下级的）
        List<Map<String, Object>> inviteTeamNumListMaps = umsUserInviteService.listMaps(new SqlQueryWrapper<UmsUserInvite>()
                .select("parent_id,COUNT(1) count").in("parent_id", inviteIds).groupBy("parent_id"));

        // 填充下级的邀请人数
        List<Boolean> collect = inviteTeamNumListMaps.stream().map(two -> inviteInfoList.stream().anyMatch(vo -> {
            Long userId = vo.getUserId();
            Long parentId = Long.valueOf(two.get("parent_id").toString());
            if (userId.equals(parentId)) {
                vo.setTeamNum(Integer.valueOf(two.get("count").toString()));
                return true;
            }
            return false;
        })).collect(Collectors.toList());

        // 填充下级的有效订单数据
        collect = inviteOrderList.stream().map(map -> inviteInfoList.stream().anyMatch(vo -> {
            Long oneInviteId = vo.getUserId();
            Long inviteId = Long.valueOf(map.get("user_id").toString());
            if (oneInviteId.equals(inviteId)) {
                vo.setConsumeNum(DecimalUtil.objToInt(map.get("count")));
                vo.setConsumeTotal(DecimalUtil.objToLong(map.get("money")));
                return true;
            }
            return false;
        })).collect(Collectors.toList());

        return inviteInfoList;
    }

}
