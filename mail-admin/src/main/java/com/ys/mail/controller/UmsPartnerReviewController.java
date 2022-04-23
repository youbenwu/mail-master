package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.UmsPartnerReview;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.QueryParentQuery;
import com.ys.mail.model.vo.UmsPartnerReviewVO;
import com.ys.mail.service.UmsPartnerReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
@RestController
@RequestMapping("/pc/ums-partner-review")
@Api(tags = "合伙人审核")
public class UmsPartnerReviewController {

    @Autowired
    private UmsPartnerReviewService partnerReviewService;

    @ApiOperation("合伙人审核-列表-DT")
    @PostMapping(value = "/list")
    public CommonResult<Page<UmsPartnerReview>> list(@Validated @RequestBody QueryParentQuery query) {
        return partnerReviewService.list(query);
    }

    @ApiOperation("合伙人审核信息详情-DT")
    @GetMapping(value = "/getInfo/{partnerReviewId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "partnerReviewId", value = "主键id", dataType = "Long", required = true)
    })
    public CommonResult<UmsPartnerReviewVO> getInfo(@PathVariable Long partnerReviewId) {
        UmsPartnerReviewVO result = partnerReviewService.getInfoById(partnerReviewId);
        return CommonResult.success(result);
    }

    @ApiOperation("审核通过-DT")
    @PostMapping(value = "/succeed/{partnerReviewId:^\\d{19}$}")
    public CommonResult<Boolean> succeed(@PathVariable Long partnerReviewId) {
        return partnerReviewService.succeed(partnerReviewId);
    }

    @ApiOperation("审核不通过-DT")
    @PostMapping(value = "/fail/{partnerReviewId:^\\d{19}$}")
    public CommonResult<Boolean> fail(@PathVariable Long partnerReviewId) {
        return partnerReviewService.fail(partnerReviewId);
    }

}
