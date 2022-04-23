package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.OmsOrderQuery;
import com.ys.mail.model.admin.vo.PcUserOrderVO;
import com.ys.mail.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-12-27
 */
@Validated
@RestController
@RequestMapping("/oms-order")
@Api(tags = "订单管理")
public class OmsOrderController {

    @Autowired
    private OmsOrderService service;

    @ApiOperation("后台订单列表")
    @GetMapping(value = "/getGeneralOrder")
    public CommonResult<Page<PcUserOrderVO>> getGeneralOrder(@Validated OmsOrderQuery query) {
        return service.getGeneralOrder(query);
    }

//    @ApiOperation(value = "订单导出", notes = "支持条件过滤")
//    @PostMapping(value = "/export")
//    public CommonResult<String> export(@Validated @RequestBody ExportOrderParam params) {
//        return service.export(params);
//    }

}
