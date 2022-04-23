package com.ys.mail.controller;

import com.ys.mail.entity.SysKdCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.KdNiaoService;
import com.ys.mail.service.SysKdCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/logistics")
@Validated
@Api(tags = "物流信息管理")
public class LogisticsController {

    @Autowired
    private KdNiaoService kdNiaoService;
    @Autowired
    private SysKdCodeService sysKdCodeService;

    @ApiOperation("获取指定的物流信息")
    @GetMapping("getLogistics")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "寄件人/收件人手机号后四位", name = "customerName", dataType = "string"),
            @ApiImplicitParam(value = "快递公司编码", name = "shipperCode", dataType = "string", required = true),
            @ApiImplicitParam(value = "物流单号", name = "logisticCode", dataType = "string", required = true)
    })
    public CommonResult getLogistics(@RequestParam(value = "customerName", required = false) String customerName,
                                     @RequestParam("shipperCode") @NotNull String shipperCode, @RequestParam("logisticCode") @NotNull String logisticCode) {
        return kdNiaoService.orderOnlineByJson(customerName, shipperCode, logisticCode);
    }

    @ApiOperation("获取快递公司的编码列表")
    @GetMapping("getShipperCodeList")
    public CommonResult<List<SysKdCode>> getShipperCodeList() {
        return sysKdCodeService.getKdCodeList();
    }

}
