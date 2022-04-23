package com.ys.mail.controller;


import cn.hutool.core.bean.BeanUtil;
import com.ys.mail.annotation.DateValidator;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.vo.UmsIncomeDimensionVO;
import com.ys.mail.model.vo.UmsIncomeSumVO;
import com.ys.mail.service.UmsIncomeService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.RegularUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
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

    @ApiOperation("用户当前收益查询")
    @PostMapping(value = "/getUmsIncomeByIdType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID，默认查询当前用户", dataType = "Long"),
            @ApiImplicitParam(name = "incomeType", value = "收益类型:0邀请收益,1秒杀收益，2支出提现，3普通收益，默认查询最新记录", dataType = "Integer")
    })
    public CommonResult<UmsIncome> getUmsIncomeByIdType(@RequestParam("userId") Long userId, @RequestParam("incomeType") Integer incomeType) {
        UmsIncome umsIncome = umsIncomeService.getUmsIncomeByIdType(userId, incomeType);
        return CommonResult.success("查询成功", umsIncome);
    }

    @ApiOperation(value = "当前用户收益汇总查询", notes = "用户当前汇总收益查询，包括普通、秒杀、邀请、总收益等，结果返回的是实际金额")
    @PostMapping(value = "/getUserIncomeSum")
    public CommonResult<UmsIncomeSumVO> getUserIncomeSumById() {
        UmsIncomeSumVO umsIncomeVO = umsIncomeService.getUmsIncomeSumById(null);
        if (BeanUtil.isEmpty(umsIncomeVO)) umsIncomeVO = new UmsIncomeSumVO();
        return CommonResult.success("查询成功", umsIncomeVO);
    }

    @ApiOperation(value = "当前用户历史收益维度查询", notes = "根据指定日期查询出当前用户的历史收益列表，带分页，所有参数可以默认不传，则表示查询当天数据")
    @PostMapping(value = "/getUmsIncomeByDimension")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime", value = "开始时间，格式：支持 年、年月、年月日，如2022、202203、20220301"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "lastIncomeId", value = "最后一条记录ID，用于分页"),
            @ApiImplicitParam(name = "lately", value = "是否开启按最近时间查询，0->不开启；1->开启，默认开启"),
            @ApiImplicitParam(name = "pageSize", value = "分页条数，默认10条，最大50条")
    })
    public CommonResult<List<UmsIncomeDimensionVO>> getUmsIncomeByDimension(@RequestParam(value = "beginTime", required = false)
                                                                            @DateValidator(dft = DateValidator.DFT.PATTERN, pattern = RegularUtil.DATE_REGEX) String beginTime,
                                                                            @RequestParam(value = "endTime", required = false)
                                                                            @DateValidator(dft = DateValidator.DFT.PATTERN, pattern = RegularUtil.DATE_REGEX) String endTime,
                                                                            @RequestParam(value = "lastIncomeId", required = false) Long lastIncomeId,
                                                                            @RequestParam(value = "lately", required = false, defaultValue = "0") String lately,
                                                                            @RequestParam(name = "pageSize", defaultValue = "10") @Range(min = 1, max = 50, message = "分页大小范围为1-50条") String pageSize) {
        return umsIncomeService.getUmsIncomeByDimension(beginTime, endTime, lastIncomeId, lately, pageSize);
    }

    @ApiOperation("根据ID查看收益详情")
    @GetMapping(value = "/getUmsIncome")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "incomeId", value = "流水收益ID")
    })
    public CommonResult<UmsIncome> getUmsIncome(@RequestParam("incomeId") @NotNull @Pattern(regexp = "^\\d{19}$") String incomeId) {
        UmsIncome umsIncome = umsIncomeService.getById(incomeId);
        if (BlankUtil.isNotEmpty(umsIncome)) {
            umsIncome.setIncome(umsIncome.getIncome() / 100);
            umsIncome.setExpenditure(umsIncome.getExpenditure() / 100);
            umsIncome.setTodayIncome(umsIncome.getTodayIncome() / 100);
            umsIncome.setAllIncome(umsIncome.getAllIncome() / 100);
            return CommonResult.success("查询成功", umsIncome);
        }
        return CommonResult.failed("查询失败");
    }

}
