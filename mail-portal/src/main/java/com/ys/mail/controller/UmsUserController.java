package com.ys.mail.controller;


import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.annotation.ApiBlock;
import com.ys.mail.annotation.BlackListPhone;
import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.*;
import com.ys.mail.model.vo.UserInviteDataVO;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.service.UmsUserService;
import com.ys.mail.util.DecimalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * app用户表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */
@RestController
@RequestMapping("/user")
@Validated
@Api(tags = "用户管理")
public class UmsUserController {

    @Autowired
    private UmsUserService userService;
    @Autowired
    private SysSettingService sysSettingService;

    @ApiOperation("发送验证码-DT")
    @GetMapping(value = "/getAuthCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号发送验证码", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型:0大尾狐->吉狐科技,1呼啦兔->桔狐科技", dataType = "Byte")
    })
    @BlackListPhone
    @LocalLockAnn(key = "getAuthCode:arg[0]", expire = 60)
    public CommonResult<String> getAuthCode(@RequestParam("phone") @NotBlank @Pattern(regexp = "^[1][3-9][0-9]{9}$") String phone,
                                            @RequestParam("type") @NotNull @Range(min = 0, max = 1) Byte type
    ) {
        return userService.getAuthCode(phone, type);
    }

    @ApiOperation("注册或登录账号-DT")
    @PostMapping(value = "/register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号发送验证码", dataType = "String", required = true),
            @ApiImplicitParam(name = "authCode", value = "验证码", dataType = "String", required = true),
            @ApiImplicitParam(name = "parentId", value = "邀请人ID", dataType = "Long", required = false)
    })
    @BlackListPhone
    @LocalLockAnn(key = "userRegister:arg[0]")
    public CommonResult<String> register(@RequestParam("phone") @NotBlank @Pattern(regexp = "^[1][3-9][0-9]{9}$") String phone,
                                         @RequestParam("authCode") @NotBlank @Pattern(regexp = "^\\d{6}$", message = "验证码错误") String authCode,
                                         @RequestParam(name = "parentId", required = false) Long parentId) {
        return userService.userRegister(phone, authCode, parentId);
    }

    @ApiOperation("一键登录-DT")
    @PostMapping(value = "/oauth")
    public CommonResult<String> oauth(@Validated @RequestBody OauthParam param) throws Exception {
        //opToken,operator,sign,type
        return userService.userOauth(param);
    }

    @ApiOperation("个人资料-DT")
    @GetMapping(value = "/info")
    public CommonResult<UmsUser> info() {
        UmsUser user = userService.info();
        return CommonResult.success(user);
    }

    @ApiOperation("退出登录-DT")
    @PostMapping(value = "/logout")
    public CommonResult<Boolean> logout() {
        // 前端清除token和一些基本信息,后端不用做任何处理
        return CommonResult.success(null);
    }

    @ApiOperation("验证手机号-DT")
    @PostMapping(value = "/verify/{phone:^[1][3-9][0-9]{9}$}/{authCode:^\\d{6}$}")
    @BlackListPhone
    public CommonResult<Boolean> verifyPhone(@PathVariable String phone,
                                             @PathVariable String authCode) {
        // 验证手机号验证码是否正确
        return userService.verifyPhone(phone, authCode);
    }

    @ApiOperation("更改手机号-DT")
    @PostMapping(value = "/change/{phone:^[1][3-9][0-9]{9}$}")
    public CommonResult<Boolean> changePhone(@PathVariable String phone) {
        // 更改手机号
        return userService.changePhone(phone);
    }

    @ApiOperation("更换用户昵称")
    @PostMapping(value = "/changeNickname")
    @ApiImplicitParam(name = "nickname", value = "昵称", dataType = "String", required = true)
    public CommonResult<Boolean> changeNickname(@RequestParam("nickname") @NotBlank @Size(max = 10) String nickname) {
        return userService.changeNickname(nickname);
    }

    @ApiOperation("更换用户头像")
    @PostMapping(value = "/changeHeadPortrait")
    @ApiImplicitParam(name = "headPortrait", value = "头像地址", dataType = "String", required = true)
    public CommonResult<Boolean> changeHeadPortrait(@RequestParam("headPortrait") @NotBlank String headPortrait) {
        return userService.changeHeadPortrait(headPortrait);
    }

    @ApiOperation("安全验证手机号码")
    @PostMapping(value = "/securityVerify")
    public CommonResult<String> securityVerifyPhone(@RequestParam("phone") @NotBlank @Pattern(regexp = "^[1][3-9][0-9]{9}$") String phone,
                                                    @RequestParam("authCode") @NotBlank @Pattern(regexp = "^\\d{6}$") String authCode) {
        return userService.securityVerifyPhone(phone, authCode);
    }

    @ApiOperation("设置支付密码")
    @PostMapping(value = "/paymentCode")
    public CommonResult<Boolean> setPaymentCode(@Validated @RequestBody UserTemPayCodeParam param) {
        return userService.setPaymentCode(param);
    }

    @ApiOperation("呼啦兔一键登录-DT")
    @PostMapping(value = "/hulatu/oauth")
    public CommonResult<String> hulatuOauth(@Validated @RequestBody OauthParam param) throws Exception {
        //opToken,operator,sign,type
        return userService.hulatuUserOauth(param);
    }

    @ApiOperation("用户绑定支付宝")
    @PostMapping(value = "/bindAlipay")
    @LocalLockAnn(key = "userBindAlipay:arg[0]")
    public CommonResult<Boolean> bindAlipay(@Validated @RequestBody BindAlipayParam param) {
        return userService.bindAlipay(param);
    }

    @ApiBlock
    @ApiOperation("用户提现到支付宝")
    @PostMapping(value = "/depositAlipay")
    @LocalLockAnn(key = "userDepositAlipay:arg[0]")
    public CommonResult<String> depositAlipay(@Validated @RequestBody DepositAlipayParam param) throws AlipayApiException {
        return userService.depositAlipay(param);
    }

    @ApiOperation("统计高级用户数量")
    @PostMapping(value = "/getSeniorUserCount")
    public CommonResult<Map<String, Object>> getSeniorUserCount() {
        // 读取付费会员价格
        Integer price = sysSettingService.getSettingValue(SettingTypeEnum.fourteen);

        // 获取当前所有高级数量
        QueryWrapper<UmsUser> qw = new QueryWrapper<>();
        qw.eq("role_id", NumberUtils.INTEGER_ONE).eq("deleted", NumberUtils.INTEGER_ZERO);
        int count = userService.count(qw);
        QueryWrapper<UmsUser> qwp = new QueryWrapper<>();
        // 获取最新支付会员
        qwp.eq("role_id", NumberUtils.INTEGER_ONE).eq("deleted", NumberUtils.INTEGER_ZERO)
           .orderByDesc("create_time,update_time").last("limit 1");
        UmsUser user = userService.getOne(qwp);

        // 封装结果
        Map<String, Object> map = new HashMap<>(4);
        map.put("total", count * 100);
        map.put("userName", user == null ? "" : user.getNickname());
        map.put("userPic", user == null ? "" : user.getHeadPortrait());
        map.put("price", DecimalUtil.strToStrForDivider(price.toString()));
        // 返回
        return CommonResult.success(map);
    }

    @ApiOperation("人脸识别数据校验接口")
    @PostMapping(value = "/verifyFace")
    @ApiImplicitParam(value = "人脸肖像数据", name = "userImageString", required = true)
    public CommonResult<Boolean> verifyFace(@RequestParam(name = "userImageString") @NotBlank String userImageString) {
        return userService.verifyFace(userImageString);
    }

    @ApiOperation("人脸识别安卓调用sdk")
    @PostMapping(value = "/callVerifyFace")
    public CommonResult<Object> callVerifyFace(@Validated @RequestBody VerifyFaceParam param) {
        // TODO 用户进行人脸识别,发送一个faceId给前端做唯一,使用aop做缓存设计,存入redis中存入一个用户id,存入三次,并且一天,第二天可以重新开始
        // TODO 存入凌晨0:00过期,安卓发起调用,我这边返回faceId给安卓,使用注解aop,
        // TODO 安卓调用sdk,先从服务器端拿到faceId,服务器做三层拦截,第一,验证三次被拦截,第二,已经人脸识别无需再人脸识别
        // TODO 大尾狐和呼啦兔的验证都是要经过这个接口,接口不需要判断是大尾狐还是呼啦兔,只需要判断用户id,用户id是唯一的且要被记录
        // TODO 比如在大尾狐验证一次,呼啦兔也验证一次,这个是会被记录的,接口始终只会返回一个结果给前端,那么需要前端调用的时候让后端调用的是哪个sdk
        // TODO 区分开是哪个sdk就ok了
        return userService.callVerifyFace(param);
    }

    @ApiOperation("查看用户邀请的统计详情")
    @GetMapping("/getUserInviteDataList")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "翻页id，可选", name = "userId"),
            @ApiImplicitParam(value = "查询的条数(默认是5，范围：1~50)", name = "pageSize", dataType = "int")
    })
    public CommonResult<UserInviteDataVO> getUserInviteDataList(@RequestParam(value = "userId", required = false)
                                                                @BlankOrPattern(regexp = "^\\d{19}$") String userId,
                                                                @RequestParam(value = "pageSize", required = false, defaultValue = "5") @Min(value = 1) @Max(value = 50) Integer pageSize) {
        UserInviteDataVO inviteDataVo = userService.getUserInviteDataList(userId, pageSize);
        return CommonResult.success(inviteDataVo);
    }
}
