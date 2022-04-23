package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PcSysDic;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.PcSysDicService;
import com.ys.mail.util.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-12-24
 */
@RestController
@RequestMapping("/pc-sys-dic")
@Api(tags = "字典管理")
public class PcSysDicController {
    @Autowired
    private PcSysDicService pcSysDicService;

    @ApiOperation("pc字典添加")
    @PutMapping(value = "/addPcSysDic")
    public CommonResult<Boolean> addPcSysDic(@RequestBody PcSysDic pcSysDic) {
        Long id = pcSysDic.getSysDicId();
        pcSysDic.setSysDicId(id.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : id);
        Boolean result = pcSysDicService.saveOrUpdate(pcSysDic);
        return CommonResult.success(result);
    }

    @ApiOperation("pc字典查询")
    @GetMapping(value = "/getPcSysDic")
    public CommonResult<Page<PcSysDic>> getPcSysDic(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        QueryWrapper<PcSysDic> queryWrapper = new QueryWrapper<>();
        Page<PcSysDic> page = new Page<>(pageNum, pageSize);
        Page<PcSysDic> result = pcSysDicService.page(page, queryWrapper);
        return CommonResult.success(result);
    }

    @ApiOperation("pc字典删除")
    @DeleteMapping(value = "/deletetPcSysDic")
    public CommonResult<Boolean> deletePcSysDic(@RequestParam(name = "withdrawalReviewId", required = true) Long withdrawalReviewId) {
        Boolean result = pcSysDicService.removeById(withdrawalReviewId);
        return CommonResult.success(result);
    }
}
