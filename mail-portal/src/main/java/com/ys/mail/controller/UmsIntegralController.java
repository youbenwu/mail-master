package com.ys.mail.controller;


import com.ys.mail.entity.UmsIntegral;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.UmsIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 积分变化历史记录表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@RestController
@RequestMapping("/integral")
@Api(tags = "用户积分管理")
public class UmsIntegralController {

    @Autowired
    private UmsIntegralService integralService;

    @ApiOperation("用户积分详情-DT")
    @GetMapping(value = "/getIntegralInfo")
    public CommonResult<UmsUser> getIntegralInfo() {
        UmsUser user = integralService.getIntegralInfo();
        return CommonResult.success(user);
    }

    @ApiOperation("积分明细-DT")
    @GetMapping(value = "/getAllIntegral/{integralId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "integralId", value = "积分明细id,首次传0,用于翻页", dataType = "Long", required = true)
    })
    public CommonResult<List<UmsIntegral>> getAllIntegral(@PathVariable Long integralId) {
        List<UmsIntegral> result = integralService.getAllIntegral(integralId);
        return CommonResult.success(result);
    }
}
