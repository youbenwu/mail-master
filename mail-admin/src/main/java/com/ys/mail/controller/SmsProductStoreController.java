package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.SmsProductStoreParam;
import com.ys.mail.model.admin.query.SmsProductStoreQuery;
import com.ys.mail.model.admin.vo.SmsProductStoreVO;
import com.ys.mail.service.SmsProductStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户_商品_店铺表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2022-04-24
 */
@RestController
@Api(tags = "用户商品店铺管理")
@RequestMapping("/product/store")
public class SmsProductStoreController {

    @Autowired
    private SmsProductStoreService smsProductStoreService;

    @ApiOperation(value = "商品店铺列表查询", notes = "分页条件查询")
    @GetMapping(value = "/getPage")
    public CommonResult<IPage<SmsProductStoreVO>> getPage(@Validated SmsProductStoreQuery query) {
        IPage<SmsProductStoreVO> page = smsProductStoreService.getPage(query);
        return CommonResult.success(page);
    }

    @ApiOperation(value = "审核用户店铺")
    @PostMapping(value = "/updateReviewState")
    public CommonResult<Boolean> updateReviewState(@Validated @RequestBody SmsProductStoreParam param) {
        boolean result = smsProductStoreService.updateReviewState(param);
        return result ? CommonResult.success(true) : CommonResult.failed(CommonResultCode.ILLEGAL_REQUEST);
    }

}
