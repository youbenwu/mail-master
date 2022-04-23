package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsPartnerRe;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.dto.PcPartnerReDTO;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.service.PmsPartnerReService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 合伙人退还期数表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2022-03-01
 */
@Api(tags = "创客商品退还期数管理")
@Validated
@RestController
@RequestMapping("/partnerRe")
public class PmsPartnerReController {

    @Autowired
    private PmsPartnerReService partnerReService;

    /**
     * 没有新增和修改,只有一个查询和修改状态不让他返还
     */
    @ApiOperation(value = "创客返还列表", notes = "支持分页、多条件查询")
    @PostMapping(value = "/list")
    public CommonResult<Page<PcPartnerReDTO>> list(@Validated @RequestBody Query qo) {
        // 全部,日期查询,用户名称匹配
        Page<PcPartnerReDTO> result = partnerReService.list(qo);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "修改返还状态")
    @PutMapping(value = "/create/{id:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "periodsStatus", value = "状态:0->未退还,2->退还失败,不能手动选择1退还成功", dataType = "Byte", required = true)
    })
    public CommonResult<Boolean> create(@PathVariable("id") Long partnerReId,
                                        @RequestParam("periodsStatus") @Pattern(regexp = "[02]") @NotBlank String periodsStatus) {
        PmsPartnerRe partnerRe = new PmsPartnerRe(partnerReId, Byte.valueOf(periodsStatus));
        return partnerReService.updateById(partnerRe) ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }

}
