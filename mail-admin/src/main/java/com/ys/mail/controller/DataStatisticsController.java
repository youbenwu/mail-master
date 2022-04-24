package com.ys.mail.controller;

import com.ys.mail.annotation.DateValidator;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.vo.statistics.EveryMonthCollectDataVo;
import com.ys.mail.model.admin.vo.statistics.InviteLadderListVo;
import com.ys.mail.model.admin.vo.statistics.PcOrderCollectDataVo;
import com.ys.mail.model.admin.vo.statistics.PcReviewCollectDataVo;
import com.ys.mail.service.DataStatisticsService;
import com.ys.mail.util.DateTool;
import io.swagger.annotations.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Desc 数据统计接口服务
 * @Author CRH
 * @Create 2022-02-21 15:40
 */
@Validated
@RestController
@Api(tags = "数据统计管理")
@RequestMapping("/dataStatisticsManage")
public class DataStatisticsController {

    @Autowired
    private DataStatisticsService dsService;

    @ApiOperation(value = "统计后台当日审核汇总数据", notes = "实时统计接口")
    @GetMapping(value = "/getReviewDataByDate")
    public CommonResult<PcReviewCollectDataVo> getReviewDataByDate() {
        String date = DateTool.getTodayNow();
        return CommonResult.success(dsService.getReviewDataByDate(date));
    }

    @ApiOperation(value = "统计后台所有订单汇总数据", notes = "缓存刷新时间：1小时")
    @GetMapping(value = "/getAllOrderData")
    @ApiResponses({
            @ApiResponse(code = 200, response = PcOrderCollectDataVo.class, message = "PcOrderCollectDataVo")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refresh", value = "是否强制刷新Token，默认0，1->表示强制刷新，不使用缓存")
    })
    public CommonResult<Map<String, Object>> getAllOrderData(@RequestParam(value = "refresh", defaultValue = "0") boolean refresh) {
        Map<String, Object> resultMap = dsService.getAllOrderData(refresh);
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "统计指定日期订单的汇总数据", notes = "缓存刷新时间：1小时")
    @GetMapping(value = "/getOrderDataByDate")
    @ApiResponses({
            @ApiResponse(code = 200, response = PcOrderCollectDataVo.class, message = "PcOrderCollectDataVo")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "date", value = "日期格式：yyyy-MM-dd，如：2022-03-02", required = true),
            @ApiImplicitParam(name = "refresh", value = "是否强制刷新Token，默认0，1->表示强制刷新，不使用缓存")
    })
    public CommonResult<Map<String, Object>> getOrderDataByDate(@RequestParam(value = "date") @DateValidator(required = true) String date,
                                                                @RequestParam(value = "refresh", defaultValue = "0") boolean refresh) {
        Map<String, Object> resultMap = dsService.getOrderDataByDate(date, refresh);
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "统计每月汇总数据", notes = "缓存刷新时间：1小时")
    @GetMapping(value = "/getEveryMonthCollectData")
    @ApiResponses({
            @ApiResponse(code = 200, response = EveryMonthCollectDataVo.class, message = "EveryMonthCollectDataVo")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "默认为1，统计类型：1->统计每月秒杀订单数据，2->统计每月提现流水数据", required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "date", value = "日期格式：yyyy-MM，如：2022-03", required = true),
            @ApiImplicitParam(name = "refresh", value = "是否强制刷新Token，默认0，1->表示强制刷新，不使用缓存")
    })
    public CommonResult<Map<String, Object>> getEveryMonthCollectData(@RequestParam(value = "type", defaultValue = "1") @Length(min = 1, max = 2) String type,
                                                                      @RequestParam(value = "date")
                                                                      @DateValidator(dft = DateValidator.DFT.YM, required = true) String date,
                                                                      @RequestParam(value = "refresh", defaultValue = "0") boolean refresh) {
        Map<String, Object> resultMap;
        if (StringConstant.ONE.equals(type)) resultMap = dsService.getSaleOrderDataByYearMonth(date, refresh);
        else resultMap = dsService.getExIncomeDataByYearMonth(date, refresh);
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "统计用户邀请天梯榜单数据", notes = "支持分页")
    @GetMapping(value = "/getUserInviteLadderList")
    @ApiResponses({
            @ApiResponse(code = 200, response = InviteLadderListVo.class, message = "InviteLadderListVo")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页条数", required = true, defaultValue = "10")
    })
    public CommonResult<Map<String, Object>> getUserInviteLadderList(@RequestParam(value = "pageNum", defaultValue = "1") String pageNum,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10") String pageSize) {
        Map<String, Object> resultMap = dsService.getUserInviteLadderList(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        return CommonResult.success(resultMap);
    }
}
