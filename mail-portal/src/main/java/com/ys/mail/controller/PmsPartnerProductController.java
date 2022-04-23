package com.ys.mail.controller;

import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.PartnerProductDTO;
import com.ys.mail.model.param.PartnerGenerateOrderParam;
import com.ys.mail.model.po.PartnerProductPO;
import com.ys.mail.service.PmsPartnerProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author ghdhj
 */
@Api(tags = "前端创客产品")
@Validated
@RestControllerAdvice
@RequestMapping("/partner/product")
public class PmsPartnerProductController {

    @Autowired
    private PmsPartnerProductService partnerProductService;

    @ApiOperation("查询创客商品列表-DT")
    @GetMapping(value = "/{more:^0|^1}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "more", value = "是否更多,少:0,多:1", dataType = "Byte", required = true),
            @ApiImplicitParam(name = "partnerPdtId", value = "合伙人主键id,用于翻页,传0即查询最新的,最后一个id进行翻页", dataType = "String")
    })
    public CommonResult<List<PartnerProductDTO>> list(@PathVariable Byte more,
                                                      @RequestParam("partnerPdtId") @Pattern(regexp = "^0|^\\d{19}$") String partnerPdtId) {
        return CommonResult.success(partnerProductService.list(more, partnerPdtId));
    }

    @ApiOperation("创客详情-DT")
    @GetMapping(value = "/info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "partnerPdtId", value = "主键id", dataType = "String")
    })
    public CommonResult<PartnerProductPO> info(@RequestParam("partnerPdtId") @Pattern(regexp = "^\\d{19}$") String partnerPdtId) {
        return CommonResult.success(partnerProductService.info(partnerPdtId));
    }

    @ApiOperation("创客立即购买页面-DT")
    @GetMapping(value = "/buy")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "partnerPdtId", value = "主键id", dataType = "String", required = true)
    })
    public CommonResult<PartnerProductDTO> buy(@RequestParam("partnerPdtId") @Pattern(regexp = "^\\d{19}$") String partnerPdtId) {
        return CommonResult.success(partnerProductService.buy(partnerPdtId));
    }

    @ApiOperation("生成订单-DT")
    @PostMapping(value = "/generateOrder")
    @LocalLockAnn(key = "partnerPdtGeOrder:arg[0]")
    public CommonResult<GenerateOrderBO> generateOrder(@Validated @RequestBody PartnerGenerateOrderParam param) {
        //生成订单
        return partnerProductService.generateOrder(param);
    }


}
