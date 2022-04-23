package com.ys.mail.controller;

import com.ys.mail.entity.PcUser;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.vo.UserImInfoVO;
import com.ys.mail.service.RongCloudService;
import com.ys.mail.service.UserManageService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.PcUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-20 09:36
 */
@Validated
@RestController
@RequestMapping("/pc/im")
@Api(tags = "后台融云IM管理")
public class PcRongCloudController {

    @Autowired
    private RongCloudService rongCloudService;
    @Autowired
    private UserManageService userManageService;

    @ApiOperation(value = "获取融云Token")
    @GetMapping(value = "/getToken")
    @ApiImplicitParam(name = "forcedRefresh", value = "是否强制刷新Token，默认0，1-表示强制刷新")
    public CommonResult<String> getToken(@RequestParam(value = "forcedRefresh", defaultValue = "0") String forcedRefresh) {
        PcUser currentUser = PcUserUtil.getCurrentUser();
        String token = rongCloudService.register(currentUser.getPcUserId(), currentUser.getUsername(), currentUser.getHeadPortrait(), forcedRefresh);
        if (BlankUtil.isEmpty(token)) return CommonResult.failed("获取融云token失败，请重试");
        return CommonResult.success(token);
    }

    @ApiOperation(value = "获取融云AppKey")
    @GetMapping(value = "/getAppKey")
    public CommonResult<String> getAppKey() {
        String appKey = rongCloudService.getAppKey();
        if (BlankUtil.isEmpty(appKey)) return CommonResult.failed("获取融云appKey失败，请重试");
        return CommonResult.success(appKey);
    }

    @ApiOperation(value = "根据ID列表获取用户信息")
    @GetMapping(value = "/getUserImInfo")
    @ApiImplicitParam(name = "userIds", value = "APP用户ID列表，以英文逗号分隔")
    public CommonResult<List<UserImInfoVO>> getUserImInfo(@RequestParam(value = "userIds") @NotEmpty @Pattern(regexp = "^[\\d]+[\\d,]+$") String userIds) {
        List<String> result = Arrays.asList(userIds.split(","));
        return userManageService.getUserImInfo(result);
    }

}
