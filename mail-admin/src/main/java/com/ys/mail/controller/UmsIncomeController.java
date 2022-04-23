package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.UmsIncomeQuery;
import com.ys.mail.model.admin.vo.UmsIncomeVO;
import com.ys.mail.service.UmsIncomeService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
@Validated
@RestController
@RequestMapping("/pc/ums-income")
@Api(tags = "收益流水管理")
public class UmsIncomeController {

    @Autowired
    private UmsIncomeService umsIncomeService;

    @ApiOperation(value = "收益流水查询", notes = "分页查询，keyword -> 收益类型:0邀请收益,1秒杀收益，2支出提现，3普通收益")
    @GetMapping(value = "/getPage")
    public CommonResult<Page<UmsIncomeVO>> getPage(@Validated UmsIncomeQuery query) {
        if (BlankUtil.isEmpty(query.getPageNum())) query.setPageNum(1);
        if (BlankUtil.isEmpty(query.getPageSize())) query.setPageSize(10);
        return umsIncomeService.getPage(query);
    }

}
