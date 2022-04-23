package com.ys.mail.controller;

import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.vo.UmsUserOrderCollectStaticsVO;
import com.ys.mail.service.UmsUserOrderCollectStaticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-07 17:26
 * @Email 18218292802@163.com
 */
@Validated
@RestController
@RequestMapping("/user/statistics")
@Api(tags = "用户中心统计信息")
public class UmsUserStatisticsController {

    @Autowired
    private UmsUserOrderCollectStaticsService staticsService;

    @ApiOperation("获取用户订单收藏的相关数量")
    @GetMapping(value = "/getCount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID，为空时默认当前用户"),
            @ApiImplicitParam(name = "cpyType", value = "公司类型订单:0->大尾狐,1->呼啦兔允许为空")
    })
    public CommonResult<UmsUserOrderCollectStaticsVO> getOrderCollectCount(@RequestParam(value = "userId", required = false) Long userId,
                                                                           @RequestParam(value = "cpyType", required = false)
                                                                           @BlankOrPattern(regexp = "^[01]$", message = "公司类型订单:0->大尾狐,1->呼啦兔") String cpyType) {
        UmsUserOrderCollectStaticsVO orderCollectStaticsInfo = staticsService.getOrderCollectStaticsInfo(userId, cpyType);
        return CommonResult.success(orderCollectStaticsInfo);
    }
}
