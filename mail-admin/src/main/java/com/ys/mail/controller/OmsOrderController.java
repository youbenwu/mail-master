package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.OmsOrderQuery;
import com.ys.mail.model.admin.vo.PcUserOrderVO;
import com.ys.mail.model.vo.OmsOrderItemVO;
import com.ys.mail.service.OmsOrderService;
import io.swagger.annotations.Api;
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

    @ApiOperation(value = "订单导出接口", notes = "该接口分页参数无效，只有其他过滤条件有效")
    @PostMapping(value = "/exportExcel")
    public void exportExcel(@Validated OmsOrderQuery query, HttpServletResponse response) {
        service.exportExcel(query, "平台订单数据", response);
    }

}
