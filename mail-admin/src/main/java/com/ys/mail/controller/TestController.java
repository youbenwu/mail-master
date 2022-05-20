package com.ys.mail.controller;

import com.ys.mail.model.CommonResult;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 测试接口
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
@Validated
@RestController
@Api(tags = "测试管理(无需权限)")
@RequestMapping("/testManage")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @PostMapping(value = "/test")
    public CommonResult<Boolean> test(@RequestParam(name = "file") MultipartFile multipartFile) {
        return CommonResult.success(true);
    }

    @PostMapping(value = "/test1")
    public CommonResult<Boolean> test1() {
        return CommonResult.success(true);
    }

}
