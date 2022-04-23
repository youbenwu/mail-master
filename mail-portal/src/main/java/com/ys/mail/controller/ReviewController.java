package com.ys.mail.controller;

import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.entity.PcReview;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.ReviewService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-08 15:17
 */
@Validated
@RestController
@RequestMapping("/review")
@Api(tags = "提现审核管理")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @ApiOperation(value = "个人提现审核列表", notes = "分页查询")
    @GetMapping(value = "/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reviewId", value = "reviewId，用于分页，默认为0"),
            @ApiImplicitParam(name = "pageSize", value = "分页条数，默认10条，最大50条")
    })
    public CommonResult<List<PcReview>> getPage(@RequestParam(name = "reviewId", required = false) String reviewId,
                                                @RequestParam(name = "pageSize", defaultValue = "10")
                                                @Range(min = 1, max = 50, message = "分页大小范围为1-50条") String pageSize) {
        if (BlankUtil.isEmpty(reviewId)) reviewId = StringConstant.STRING_ZERO;
        List<PcReview> list = reviewService.selectList(Long.valueOf(reviewId), Long.valueOf(pageSize));
        return CommonResult.success(list);
    }

    @ApiOperation(value = "手动取消提现审核")
    @GetMapping(value = "/cancel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reviewId", value = "审核记录主键，用于取消审核", required = true)
    })
    @LocalLockAnn(key = "review-cancel:arg[0]")
    public CommonResult<Boolean> cancel(@RequestParam(name = "reviewId") @Pattern(regexp = "^\\d{19}$") String reviewId) {
        return reviewService.cancel(Long.valueOf(reviewId));
    }

}
