package com.ys.mail.controller;


import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.UmsPartnerReviewDTO;
import com.ys.mail.service.UmsPartnerReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/ums-partner-review")
@Api(tags = "APP合伙人审核")
public class UmsPartnerReviewController {

    @Autowired
    private UmsPartnerReviewService partnerReviewService;

    @ApiOperation("合伙人申请")
    @PostMapping("/apply")
    public CommonResult<String> apply(@RequestBody UmsPartnerReviewDTO req) {
        return partnerReviewService.apply(req);
    }

    @ApiOperation("合伙人判断")
    @PostMapping("/judge")
    public CommonResult<Map<String, Object>> judge() {
        return partnerReviewService.judge();
    }

}
