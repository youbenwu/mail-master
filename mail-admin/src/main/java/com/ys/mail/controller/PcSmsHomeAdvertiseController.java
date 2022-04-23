package com.ys.mail.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.SmsHomeAdvertise;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.HomeAdvertiseParam;
import com.ys.mail.model.admin.query.HomeAdvertiseQuery;
import com.ys.mail.service.SmsHomeAdvertiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-02-12 13:42
 */
@RestController
@RequestMapping("/pc/advertiseManage")
@Api(tags = "后台轮播图管理")
public class PcSmsHomeAdvertiseController {

    @Autowired
    private SmsHomeAdvertiseService homeAdvertiseService;

    @ApiOperation("轮播图列表")
    @GetMapping(value = "/list")
    public CommonResult<Page<SmsHomeAdvertise>> list(@Validated HomeAdvertiseQuery query) {
        Page<SmsHomeAdvertise> result = homeAdvertiseService.list(query);
        return CommonResult.success(result);
    }

    @ApiOperation("轮播图详情")
    @GetMapping(value = "/getInfo/{homeAdvId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "homeAdvId", value = "轮播图主键id", dataType = "Long", required = true)
    })
    public CommonResult<SmsHomeAdvertise> getInfo(@PathVariable Long homeAdvId) {
        SmsHomeAdvertise result = homeAdvertiseService.getById(homeAdvId);
        return CommonResult.success(result);
    }

    @ApiOperation("新增或修改轮播图")
    @PostMapping(value = "/create")
    public CommonResult<Boolean> create(@Validated @RequestBody HomeAdvertiseParam param) {
        return homeAdvertiseService.create(param);
    }

    @ApiOperation("修改轮播图状态")
    @PutMapping(value = "/updateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "homeAdvId", value = "轮播图主键id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "homeAdvStatus", value = "上下线状态：0->下线；1->上线", dataType = "Boolean", required = true)
    })
    public CommonResult<Boolean> updateStatus(@RequestParam("homeAdvId") @NotBlank @Pattern(regexp = "^\\d{19}$") String homeAdvId,
                                              @RequestParam("homeAdvStatus") @NotNull Boolean homeAdvStatus) {
        SmsHomeAdvertise build = SmsHomeAdvertise.builder()
                .homeAdvId(Long.valueOf(homeAdvId))
                .homeAdvStatus(homeAdvStatus)
                .build();
        boolean b = homeAdvertiseService.updateById(build);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("删除轮播图")
    @DeleteMapping(value = "/delete/{homeAdvId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "homeAdvId", value = "轮播图主键id", dataType = "Long", required = true)
    })
    public CommonResult<Boolean> delete(@PathVariable Long homeAdvId) {
        boolean b = homeAdvertiseService.delete(homeAdvId);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

}
