package com.ys.mail.controller;

import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.SysProductLogDTO;
import com.ys.mail.service.PcSysProductLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-07 11:29
 * @Email 18218292802@163.com
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/pc/sys/product/log")
@Api(tags = "用户商品足迹管理")
public class PcSysProductLogController {

    final
    private PcSysProductLogService sysProductLogService;

    @ApiOperation("商品足迹查询-DT")
    @GetMapping(value = "/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long"),
            @ApiImplicitParam(name = "productLogId", value = "商品足迹ID", dataType = "Long")
    })
    public CommonResult<List<SysProductLogDTO>> getAllProductLogInfo(@RequestParam(name = "userId", required = false) Long userId,
                                                                     @RequestParam(name = "productLogId", required = false) Long productLogId) {

        List<SysProductLogDTO> result = sysProductLogService.selectAllProductLog(userId, productLogId);
        return CommonResult.success(result);
    }

}
