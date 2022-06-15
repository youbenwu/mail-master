package com.ys.mail.controller;


import cn.hutool.core.bean.BeanUtil;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.query.IncomeDimensionQuery;
import com.ys.mail.model.vo.UmsIncomeDimensionVO;
import com.ys.mail.model.vo.UmsIncomeSumVO;
import com.ys.mail.model.vo.UmsIncomeVO;
import com.ys.mail.service.UmsIncomeService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.DecimalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * <p>
 * 收益表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-25
 */
@Validated
@RestController
@RequestMapping("/ums-income")
@Api(tags = "用户收益")
public class UmsIncomeController {

    @Autowired
    private UmsIncomeService umsIncomeService;

    @PostMapping(value = "/getUserIncomeSum")
    @ApiOperation(value = "收益汇总查询", notes = "包括普通、秒杀、邀请、总收益等，结果返回的是实际金额")
    public CommonResult<UmsIncomeSumVO> getUserIncomeSumById() {
        UmsIncomeSumVO umsIncomeVO = umsIncomeService.getUmsIncomeSumById(null);
        if (BeanUtil.isEmpty(umsIncomeVO)) {
            umsIncomeVO = new UmsIncomeSumVO();
        }
        return CommonResult.success("查询成功", umsIncomeVO);
    }

    @ApiOperation(value = "历史收益查询", notes = "根据指定日期查询出当前用户的历史收益列表，带分页，所有参数可以默认不传，则表示查询当天数据")
    @PostMapping(value = "/getUmsIncomeByDimension")
    public CommonResult<List<UmsIncomeDimensionVO>> getUmsIncomeByDimension(@Validated IncomeDimensionQuery query) {
        return umsIncomeService.getUmsIncomeByDimension(query);
    }

    @ApiOperation("收益详情")
    @GetMapping(value = "/getUmsIncome")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "incomeId", value = "流水收益ID")
    })
    public CommonResult<UmsIncomeVO> getUmsIncome(@RequestParam("incomeId") @NotNull @Pattern(regexp = "^\\d{19}$") String incomeId) {
        UmsIncome umsIncome = umsIncomeService.getById(incomeId);
        if (BlankUtil.isNotEmpty(umsIncome)) {
            UmsIncomeVO vo = new UmsIncomeVO();
            BeanUtils.copyProperties(umsIncome, vo);
            vo.setIncome(DecimalUtil.longToDoubleForDivider(umsIncome.getIncome()));
            vo.setExpenditure(DecimalUtil.longToDoubleForDivider(umsIncome.getExpenditure()));
            vo.setOriginal(DecimalUtil.longToDoubleForDivider(umsIncome.getOriginal()));
            vo.setIntegral(DecimalUtil.longToDoubleForDivider(umsIncome.getIntegral()));
            vo.setBalance(DecimalUtil.longToDoubleForDivider(umsIncome.getBalance()));
            vo.setTodayIncome(DecimalUtil.longToDoubleForDivider(umsIncome.getTodayIncome()));
            vo.setAllIncome(DecimalUtil.longToDoubleForDivider(umsIncome.getAllIncome()));
            return CommonResult.success(vo);
        }
        return CommonResult.failed();
    }

}
