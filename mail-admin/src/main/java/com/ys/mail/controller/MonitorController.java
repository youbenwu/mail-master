package com.ys.mail.controller;

import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Desc 系统监控服务
 * @Author CRH
 * @Create 2022-03-07 16:10
 */
@RestController
@Api(tags = "系统监控管理")
@RequestMapping("/sys/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @GetMapping(value = "getServers")
    @ApiOperation(value = "获取系统监控信息", notes = "缓存刷新时间：1分钟，可定时刷新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refresh", value = "是否强制刷新Token，默认0，1->表示强制刷新，不使用缓存")
    })
    public CommonResult<Map<String, Object>> getServers(@RequestParam(value = "refresh", defaultValue = "0") boolean refresh) {
        Map<String, Object> servers = monitorService.getServers(refresh);
        return CommonResult.success(servers);
    }

}
