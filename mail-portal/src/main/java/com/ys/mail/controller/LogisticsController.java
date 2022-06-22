package com.ys.mail.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ys.mail.entity.SysKdCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.KdNiaoService;
import com.ys.mail.service.KdService;
import com.ys.mail.service.SysKdCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/logistics")
@Validated
@Api(tags = "物流信息管理")
public class LogisticsController {

    @Autowired
    private SysKdCodeService sysKdCodeService;
    @Autowired
    private KdService kdService;

    @ApiOperation("获取指定的物流信息")
    @GetMapping("getLogistics")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "寄件人/收件人手机号后四位", name = "customerName", dataType = "string"),
            @ApiImplicitParam(value = "物流单号", name = "logisticCode", dataType = "string", required = true)
    })
    public CommonResult<JSONObject> getLogistics(@RequestParam(value = "customerName", required = false) String customerName,
                                                 @RequestParam("logisticCode") @NotBlank String logisticCode) {
        JSONArray logistics = kdService.getLogistics(logisticCode);
        JSONObject result = kdService.logisticsTrack(logisticCode, ((JSONObject) logistics.get(NumberUtils.INTEGER_ZERO)).get("ShipperCode").toString(), customerName);
        return CommonResult.success(result);
    }

    // @ApiOperation("获取快递公司的编码列表")
    // @GetMapping("getShipperCodeList")
    // public CommonResult<List<SysKdCode>> getShipperCodeList() {
    //     return sysKdCodeService.getKdCodeList();
    // }

}
