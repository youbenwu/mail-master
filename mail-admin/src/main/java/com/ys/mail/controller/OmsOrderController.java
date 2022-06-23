package com.ys.mail.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.OmsOrderQuery;
import com.ys.mail.model.admin.vo.PcUserOrderVO;
import com.ys.mail.model.vo.OmsOrderItemVO;
import com.ys.mail.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
@RequestMapping("/orderManager")
@Api(tags = "订单管理")
public class OmsOrderController {

    @Autowired
    private OmsOrderService service;

    @ApiOperation("后台订单列表")
    @GetMapping(value = "/getPage")
    public CommonResult<Page<PcUserOrderVO>> getPage(@Validated OmsOrderQuery query) {
        return service.getPage(query);
    }

    @ApiOperation("查看订单详情")
    @GetMapping(value = "/orderItem/{orderId:^\\d+$}")
    public CommonResult<List<OmsOrderItemVO>> getOrderItem(@PathVariable Long orderId) {
        List<OmsOrderItemVO> list = service.getItemList(orderId);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "订单导出接口", notes = "该接口分页参数无效，只有其他过滤条件有效，最大查询2000条")
    @PostMapping(value = "/exportExcel")
    public void exportExcel(@Validated OmsOrderQuery query, HttpServletResponse response) {
        service.exportExcel(query, "平台订单数据", response);
    }

    /**
     * 后台管理人员添加物流单号,物流单号,物流编码,订单id
     */
    @ApiOperation("添加物流单号")
    @PostMapping(value = "/{id}/logistics")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "deliverySn", value = "物流单号", dataType = "String", required = true)
    })
    public CommonResult<Boolean> logistics(@PathVariable("id") Long orderId,
                                           @RequestParam("deliverySn") String deliverySn){
        boolean rsp = service.logistics(orderId,deliverySn);
        return rsp ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }

    @ApiOperation("查询物流单号的快递公司")
    @GetMapping(value = "/{deliverySn}/logistics")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverySn", value = "物流单号", dataType = "String", required = true)
    })
    public CommonResult<String> logistics(@PathVariable String deliverySn){
        return CommonResult.success(service.logistics(deliverySn));
    }


    @ApiOperation("根据快递单号查询物流轨迹")
    @GetMapping(value = "/{deliverySn}/logisticsTrack")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliverySn", value = "物流单号", dataType = "String", required = true),
            @ApiImplicitParam(name = "customerName", value = "手机号后四位,为顺丰时必填,我其它的可不填", dataType = "String"),
    })
    public CommonResult<JSONObject> logisticsTrack(@PathVariable String deliverySn,
                                                   @RequestParam(value = "customerName",required = false)  String customerName){
        return CommonResult.success(service.logisticsTrack(deliverySn,customerName));
    }
}
