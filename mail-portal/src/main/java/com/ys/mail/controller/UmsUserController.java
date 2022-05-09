package com.ys.mail.controller;


import afu.org.checkerframework.checker.oigj.qual.O;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.annotation.*;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.*;
import com.ys.mail.model.vo.UmsUserVo;
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
            @ApiImplicitParam(name = "type", value = "类型:0->轻创营,1卖乐吧", dataType = "Byte")
    })
    @BlackListPhone
    @LocalLockAnn(key = "getAuthCode:arg[0]", expire = 60)
    public CommonResult<String> getAuthCode(@RequestParam("phone") @Pattern(regexp = "^[1][3-9][0-9]{9}$") String phone,
                                            @RequestParam(value = "type", defaultValue = "0") @Range(min = 0, max = 1) Byte type
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
    public CommonResult<UmsUserVo> info() {
        UmsUserVo user = userService.info();
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
        // 获取最近支付过的一位会员
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

    @ApiOperation("人脸识别调用sdk")
    @PostMapping(value = "/callVerifyFace")
    @OftenReqAnn(key="userCallVerifyFace:arg[0]")
    public CommonResult<JSONObject> callVerifyFace(@Validated @RequestBody VerifyFaceParam param){
        // 用注解判断,一天只能5次,不能一直刷,
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
