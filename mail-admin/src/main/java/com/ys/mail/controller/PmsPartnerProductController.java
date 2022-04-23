package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsPartnerProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcPartnerProductParam;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.service.PmsPartnerProductService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 合伙人产品表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2022-02-24
 */
@Api(tags = "创客产品设置")
@Validated
@RestController
@RequestMapping("/partnerProduct")
public class PmsPartnerProductController {

    @Autowired
    private PmsPartnerProductService partnerProductService;


    @ApiOperation(value = "创客产品列表", notes = "支持分页、多条件查询")
    @PostMapping(value = "/list")
    public CommonResult<Page<PmsPartnerProduct>> list(@Validated @RequestBody Query qo) {
        LambdaQueryWrapper<PmsPartnerProduct> wrapper = Wrappers.<PmsPartnerProduct>lambdaQuery().like(BlankUtil.isNotEmpty(qo.getKeyword()), PmsPartnerProduct::getPartnerName, qo.getKeyword()).orderByDesc(PmsPartnerProduct::getPartnerPdtId);
        Page<PmsPartnerProduct> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        return CommonResult.success(partnerProductService.page(page, wrapper));
    }

    @ApiOperation(value = "创客新增和修改-DT")
    @PostMapping(value = "/create")
    public CommonResult<Boolean> create(@Validated @RequestBody PcPartnerProductParam param) {
        return partnerProductService.create(param);
    }

    @ApiOperation(value = "创客产品是否启用-DT")
    @PutMapping(value = "/create/{partnerPdtId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "partnerPdtId", value = "id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "publishStatus", value = "是否启用", dataType = "Boolean", required = true)
    })
    public CommonResult<Boolean> create(@PathVariable Long partnerPdtId,
                                        @RequestParam("publishStatus") @NotNull Boolean publishStatus) {
        boolean update = partnerProductService.updateById(new PmsPartnerProduct(partnerPdtId, publishStatus));
        return update ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }

}
