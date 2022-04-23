package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ys.mail.annotation.ApiBlock;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.entity.SysSetting;
import com.ys.mail.enums.EnumSettingType;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcReviewParam;
import com.ys.mail.model.admin.query.PcReviewQuery;
import com.ys.mail.model.admin.vo.PcReviewVO;
import com.ys.mail.service.PcReviewService;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

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
@RequestMapping("/pc-review")
@Api(tags = "提现审核管理")
public class PcReviewController {

    @Autowired
    private PcReviewService pcReviewService;
    @Autowired
    private SysSettingService sysSettingService;

    @ApiOperation(value = "提现审核查询", notes = "分页查询")
    @GetMapping(value = "/getPage")
    public CommonResult<IPage<PcReviewVO>> getPage(@Validated PcReviewQuery query) {
        if (BlankUtil.isEmpty(query.getPageNum())) query.setPageNum(1);
        if (BlankUtil.isEmpty(query.getPageSize())) query.setPageSize(10);
        return pcReviewService.getPage(query);
    }

    /**
     * 更新接口名称需要更新jwt名单
     *
     * @param param 请求参数
     * @return 结果
     */
    @ApiBlock
    @ApiOperation("提现审核更新")
    @PostMapping(value = "/updateReview")
    @LocalLockAnn(key = "pcUpdateReview:arg[0]")
    public CommonResult<Boolean> updateReview(@Validated @RequestBody PcReviewParam param) {
        return pcReviewService.updateReview(param);
    }

    @ApiOperation("提现审核删除")
    @DeleteMapping(value = "/{reviewId:^\\d{19}$}")
    public CommonResult<Boolean> delete(@PathVariable Long reviewId) {
        Boolean result = pcReviewService.removeById(reviewId);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "获取审核相关的设置")
    @GetMapping(value = "/getRelatedSetting")
    public CommonResult<List<SysSetting>> getRelatedSetting() {
        List<SysSetting> settingList = sysSettingService.getMatchList(Arrays.asList(EnumSettingType.twelve, EnumSettingType.thirteen));
        return CommonResult.success(settingList);
    }

    @ApiOperation(value = "导出提现审核数据", notes = "该接口分页参数无效，只有其他过滤条件有效")
    @PostMapping(value = "/exportExcel")
    public void exportExcel(@Validated PcReviewQuery query, HttpServletResponse response) {
        pcReviewService.exportExcel(query, "用户提现审核数据", response);
    }

}
