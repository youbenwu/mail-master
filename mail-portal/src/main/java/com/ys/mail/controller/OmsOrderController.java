package com.ys.mail.controller;


import com.ys.mail.entity.OmsOrder;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.OrderInfoDTO;
import com.ys.mail.model.dto.QuickOrderDTO;
import com.ys.mail.model.param.GiftOrderParam;
import com.ys.mail.model.query.QuickOrderQuery;
import com.ys.mail.model.vo.OmsOrderVO;
import com.ys.mail.service.OmsOrderService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@RestController
@Validated
@RequestMapping("/oms-order")
@Api(tags = "订单购管理")
public class OmsOrderController {
    @Autowired
    private OmsOrderService omsOrderService;

    @ApiOperation("订单查询")
    @GetMapping(value = "/getOmsOrderList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderStatus", value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单；-1->全部；6->已付款；7->待评价", dataType = "Long", required = true),
            @ApiImplicitParam(name = "cpyType", value = "平台类型", dataType = "int", required = true),
            @ApiImplicitParam(name = "productName", value = "商品名称", dataType = "Long", required = false)
    })
    public CommonResult<List<OmsOrderVO>> getOmsOrderList(@RequestParam(name = "orderStatus") int orderStatus,
                                                          @RequestParam(name = "orderId") Long orderId,
                                                          @RequestParam(name = "cpyType") Boolean cpyType,
                                                          @RequestParam(name = "productName", required = false) String productName) {
        List<OmsOrderVO> result = omsOrderService.getOrderList(orderStatus, orderId, cpyType, productName);
        return CommonResult.success(result);
    }

    @ApiOperation("删除订单")
    @GetMapping(value = "/delOmsOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "Long", required = true)
    })
    public CommonResult<Boolean> delOmsOrder(@Validated @RequestParam(name = "orderId") Long orderId) {
        // todo 危险性操作,不能删除订单
        return CommonResult.failed("订单不能删除");
      /*  boolean result = omsOrderService.removeById(orderId);
        if (result) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed(BusinessErrorCode.ORDER_DELETE_FAILED);
        }*/
    }

    @ApiOperation("订单详情-DT")
    @GetMapping(value = "/get/{orderSn:^\\d+$}/info")
    public CommonResult<OrderInfoDTO> getOrderInfo(@PathVariable String orderSn) {
        OrderInfoDTO result = omsOrderService.getNewOrderInfo(orderSn);
        return BlankUtil.isEmpty(result) ? CommonResult.failed("没有此订单消息") : CommonResult.success(result);
    }

    @ApiOperation("修改订单状态")
    @PostMapping(value = "/updateOrderState")
    public CommonResult<Boolean> updateOrderState(@RequestBody OmsOrder order) {
        Boolean result = omsOrderService.updateById(order);
        return CommonResult.success(result);
    }

    // @RequestParam(name = "cpyType") @NotBlank @Pattern(regexp = "^[01]$") String cpyType
    @ApiOperation("生成高级会员支付订单(礼品订单)")
    @PostMapping(value = "/generateGiftOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "人脸肖像数据", name = "userImageString", required = true)
    })
    public CommonResult<GenerateOrderBO> generateGiftOrder(@RequestParam(name = "userImageString") @NotBlank String userImageString) {

        return omsOrderService.generateGiftOrder(userImageString, "0");
    }

    @ApiOperation("更新高级会员支付订单(礼品订单)")
    @PostMapping(value = "/updateGiftOrder")
    public CommonResult<Boolean> updateGiftOrder(@Validated @RequestBody GiftOrderParam param) {
        return omsOrderService.updateGiftOrder(param);
    }

    @ApiOperation("查询秒杀订单-DT")
    @PostMapping(value = "/getAllQuickOrder")
    public CommonResult<List<QuickOrderDTO>> getAllQuickOrder(@Validated @RequestBody QuickOrderQuery query) {
        // keyword,翻页id,类型,没有了,翻页条数
        List<QuickOrderDTO> result = omsOrderService.getAllQuickOrder(query);
        return CommonResult.success(result);
    }

    @ApiOperation("查询创客订单-DT")
    @PostMapping(value = "/getAllMakerOrder")
    public CommonResult<List<QuickOrderDTO>> getAllMakerOrder(@Validated @RequestBody QuickOrderQuery query) {

        return CommonResult.success(omsOrderService.getAllMakerOrder(query));
    }
}
