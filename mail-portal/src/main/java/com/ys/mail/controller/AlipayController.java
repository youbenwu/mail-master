package com.ys.mail.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.AlipayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-04 10:07
 */
@RestController
@RequestMapping("/alipay")
@Validated
@Api(tags = "支付宝支付管理")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;


    @ApiOperation("单笔支付测试接口-DT")
    @PostMapping(value = "/test")
    @LocalLockAnn(key = "test:arg[0]")
    public CommonResult<JSONObject> test(@RequestParam("transAmount") @NotBlank @Pattern(regexp = "^([1-9]\\d*|0)(\\.\\d{1,2})?$") String transAmount) throws AlipayApiException {
        return alipayService.test(transAmount);
    }

  /*  @ApiOperation("支付宝支付成功回调地址")
    @PostMapping(value = "/notifyUrl")
    public String notifyUrl(HttpServletRequest request, HttpServletResponse response){
        return alipayService.notifyUrl(request,response);
    }*/

}
