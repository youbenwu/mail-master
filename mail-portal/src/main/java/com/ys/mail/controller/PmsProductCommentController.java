package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.ProductCommentParam;
import com.ys.mail.model.vo.PmsProductCommentVO;
import com.ys.mail.service.OmsOrderItemService;
import com.ys.mail.service.PmsProductCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * <p>
 * 商品评价表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
@RestController
@RequestMapping("/product/comment")
@Validated
@Api(tags = "商品评论管理")
public class PmsProductCommentController {

    @Autowired
    private PmsProductCommentService productCommentService;
    @Autowired
    private OmsOrderItemService omsOrderItemService;

    @ApiOperation("商品评价-DT")
    @PostMapping(value = "/saveComment")
    public CommonResult<Boolean> saveComment(@Validated @RequestBody List<ProductCommentParam> param, @RequestParam("orderId") Long orderId) {
        // 前端列表显示成待评论时才可以评论
        return productCommentService.saveComment(param, orderId);
    }

    @ApiOperation("获取当前商品的所有评论")
    @GetMapping(value = "/getAllComment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", required = true),
            @ApiImplicitParam(name = "pdtCommentId", value = "评论id,用于分页，默认为0,每次传最后一个"),
            @ApiImplicitParam(name = "pageSize", value = "分页条数，默认10条，最大50条")
    })
    public CommonResult<List<PmsProductCommentVO>> getAllComment(@RequestParam("productId") @NotBlank @Pattern(regexp = "^\\d{19}$") String productId,
                                                                 @RequestParam(value = "pdtCommentId", defaultValue = "0") Long pdtCommentId,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") @Range(min = 1, max = 50, message = "分页大小范围为1-50条") String pageSize) {
        List<PmsProductCommentVO> result = productCommentService.getProductComment(productId, pdtCommentId, Integer.valueOf(pageSize));
        return CommonResult.success(result);
    }

    @ApiOperation("获取商品的评论总数量")
    @GetMapping(value = "/getCount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", required = true)
    })
    public CommonResult<Integer> getProductCommentCount(@RequestParam("productId") @NotBlank @Pattern(regexp = "^\\d{19}$") String productId) {
        return CommonResult.success(productCommentService.getProductCommentCount(productId));
    }

    @ApiOperation("获取我的评论信息")
    @GetMapping(value = "/getProductComment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "是否评价,商品在已完成收货后评价就是1，未评价就是0", required = true)
    })
    public CommonResult<List<OmsOrderItem>> getProductComment() {
        QueryWrapper<OmsOrderItem> qw = new QueryWrapper<>();
        qw.eq("is_appraise", NumberUtils.INTEGER_ONE).orderByDesc("create_time");
        List<OmsOrderItem> result = omsOrderItemService.list(qw);
        return CommonResult.success(result);
    }
}
