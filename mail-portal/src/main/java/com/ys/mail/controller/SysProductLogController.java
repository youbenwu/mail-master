package com.ys.mail.controller;


import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.SysProductLogDTO;
import com.ys.mail.service.SysProductLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 商品足迹表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-19
 */
@RestController
@RequestMapping("/product/log")
@Validated
@Api(tags = "商品足迹管理")
public class SysProductLogController {

    @Autowired
    private SysProductLogService productLogService;

    @ApiOperation("我的足迹-DT")
    @GetMapping(value = "/getAll/{productLogId}")
    public CommonResult<List<SysProductLogDTO>> getAllProductLog(@PathVariable Long productLogId) {
        List<SysProductLogDTO> result = productLogService.getAllProductLog(productLogId);
        return CommonResult.success(result);
    }

    @ApiOperation("足迹批量删除-DT")
    @PostMapping(value = "/batchDel")
    public CommonResult<Boolean> batchDel(@RequestParam("ids") @NotEmpty List<Long> ids) {
        // 足迹列表批量删除
        return productLogService.batchDelProductLog(ids);
    }
}
