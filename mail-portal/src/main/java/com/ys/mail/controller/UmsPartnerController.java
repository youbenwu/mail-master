package com.ys.mail.controller;


import com.ys.mail.entity.PmsVerificationRecords;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.DetailQuery;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.dto.OrderDetailDto;
import com.ys.mail.model.dto.OrderInfoDTO;
import com.ys.mail.model.dto.PartnerAddressDTO;
import com.ys.mail.model.vo.ElectronicVo;
import com.ys.mail.model.vo.MerchandiseVo;
import com.ys.mail.model.vo.PartnerTodayResultsVO;
import com.ys.mail.service.UmsPartnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
@RestController
@RequestMapping("/ums-partner")
@Api(tags = "商家助手")
public class UmsPartnerController {

    @Autowired
    private UmsPartnerService partnerService;

    /**
     * 今日业绩
     */
    @GetMapping("today-results")
    @ApiOperation("今日业绩")
    public CommonResult<PartnerTodayResultsVO> todayResults() {
        return partnerService.todayResults();
    }

    @PostMapping("merchandise")
    @ApiOperation("上架库存")
    public CommonResult<MerchandiseVo> merchandise(@RequestBody Query query) {
        return partnerService.merchandise(query);
    }

    @PostMapping("/electronic")
    @ApiOperation("电子券交易")
    public CommonResult<ElectronicVo> electronic(@RequestBody Query query) {
        return partnerService.electronic(query);
    }

    @PostMapping("/verification")
    @ApiOperation("核销接口")
    public CommonResult<Boolean> verification(@RequestBody Map<String, String> params) {
        return partnerService.verification(params);
    }

    @PostMapping("/query-detail")
    @ApiOperation("查询交易明细")
    public CommonResult<PmsVerificationRecords> queryDetail(@RequestBody DetailQuery query) {
        return partnerService.queryDetail(query);
    }

    @GetMapping("/verif-detail/{recordId}")
    @ApiOperation("核销详情")
    public CommonResult<OrderDetailDto> verifDetail(@PathVariable Long recordId) {
        return partnerService.verifDetail(recordId);
    }

    @PostMapping("/verification-list")
    @ApiOperation("核销记录列表")
    public CommonResult<PmsVerificationRecords> verificationList(@RequestBody Query query) {
        return partnerService.verificationList(query);
    }


    @GetMapping("/order-detail/{orderId}")
    @ApiOperation("查询订单详情")
    public CommonResult<OrderInfoDTO> orderDetail(@PathVariable Long orderId) {
        return partnerService.orderDetail(orderId);
    }

    @GetMapping("/product/{productId}")
    @ApiOperation("根据商品ID获取供应商信息")
    @ApiImplicitParam(name = "productId", value = "合伙人商品ID", dataType = "Long")
    public CommonResult<PartnerAddressDTO> getAddressByProductId(@PathVariable Long productId) {
        PartnerAddressDTO result = partnerService.getAddressByProductId(productId);
        return CommonResult.success(result);
    }

}
