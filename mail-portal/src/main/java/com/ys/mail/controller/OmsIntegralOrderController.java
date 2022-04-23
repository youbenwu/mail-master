package com.ys.mail.controller;


import com.ys.mail.entity.OmsIntegralOrder;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.OmsIntegralOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 积分兑换订单表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@RestController
@RequestMapping("/integral/order")
@Api(tags = "积分换购管理")
public class OmsIntegralOrderController {

    @Autowired
    private OmsIntegralOrderService integralOrderService;

    @ApiOperation("积分兑换记录-DT")
    @GetMapping(value = "/getAllIntegralOrder/{integralOrderId}")
    public CommonResult<List<OmsIntegralOrder>> getAllIntegralOrder(@PathVariable Long integralOrderId) {
        List<OmsIntegralOrder> result = integralOrderService.getAllIntegralOrder(integralOrderId);
        return CommonResult.success(result);
    }
}
