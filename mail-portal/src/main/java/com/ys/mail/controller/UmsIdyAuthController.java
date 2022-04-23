package com.ys.mail.controller;


import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.IdyAuthParam;
import com.ys.mail.service.UmsIdyAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户身份认证 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@RestController
@RequestMapping("/idy/auth")
@Api(tags = "用户身份认证管理")
public class UmsIdyAuthController {

    @Autowired
    private UmsIdyAuthService idyAuthService;

    @ApiOperation("用户身份认证-DT")
    @PostMapping(value = "/createIdy")
    public CommonResult<Boolean> createIdy(@Validated @RequestBody IdyAuthParam param) {
        return idyAuthService.createIdy(param);
    }

}
