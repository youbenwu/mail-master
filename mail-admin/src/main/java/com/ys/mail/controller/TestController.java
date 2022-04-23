package com.ys.mail.controller;

import com.ys.mail.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-03-14 11:36
 */
@Validated
@RestController
@Api(tags = "测试管理(无需权限)")
@RequestMapping("/testManage")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @ApiOperation(value = "测试1")
    @PostMapping(value = "/test1")
    public CommonResult<Boolean> test1() {
        return CommonResult.success(true);
    }

}
