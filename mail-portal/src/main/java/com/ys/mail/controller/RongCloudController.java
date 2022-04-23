package com.ys.mail.controller;

import com.ys.mail.entity.UmsUser;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.vo.UserImInfoVO;
import com.ys.mail.service.RongCloudService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-13 14:56
 */
@Validated
@RestController
@RequestMapping("/im")
@Api(tags = "融云IM管理")
public class RongCloudController {

    @Autowired
    private RongCloudService rongCloudService;

    @ApiOperation("获取融云Token")
    @GetMapping(value = "/getToken")
    @ApiImplicitParam(name = "forcedRefresh", value = "是否强制刷新Token，默认0，1-表示强制刷新")
    public CommonResult<String> getToken(@RequestParam(name = "forcedRefresh", defaultValue = "0") String forcedRefresh) {
        UmsUser currentUser = UserUtil.getCurrentUser();
        Long userId = currentUser.getUserId();
        String nickname = currentUser.getNickname();
        String headPortrait = currentUser.getHeadPortrait();
        // 将User信息传过来，获取token封装返回
        String token = rongCloudService.register(userId, nickname, headPortrait, forcedRefresh);
        if (BlankUtil.isNotEmpty(token)) return CommonResult.success(token);
        return CommonResult.failed("注册失败，请联系客服");
    }

    @ApiOperation("获取融云AppKey")
    @GetMapping(value = "/getAppKey")
    public CommonResult<String> getToken() {
        String appKey = rongCloudService.getAppKey();
        if (BlankUtil.isNotEmpty(appKey)) return CommonResult.success(appKey);
        return CommonResult.failed("获取appKey失败");
    }

    @ApiOperation("获取客服列表")
    @GetMapping(value = "/getStaff")
    public CommonResult<List<UserImInfoVO>> getStaff() {
        List<UserImInfoVO> staffList = rongCloudService.getStaff();
        if (BlankUtil.isNotEmpty(staffList)) return CommonResult.success(staffList);
        return CommonResult.failed("获取客服列表失败");
    }

}
