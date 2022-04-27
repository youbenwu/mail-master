package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.annotation.EnumContains;
import com.ys.mail.entity.SysSetting;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.SysSettingParam;
import com.ys.mail.model.admin.query.SysSettingQuery;
import com.ys.mail.model.admin.vo.SysSettingVo;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.EnumTool;
import com.ys.mail.util.PcUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 全局设置表（可以新增，但只允许修改部分字段） 前端控制器
 * </p>
 *
 * @author 007
 * @since 2022-02-14
 */
@Validated
@RestController
@Api(tags = "系统设置管理")
@RequestMapping("/sys/settingManage")
public class SysSettingController {

    @Autowired
    private SysSettingService sysSettingService;

    @ApiOperation(value = "系统设置分页查询", notes = "支持分页、多条件查询")
    @GetMapping(value = "/getPage")
    public CommonResult<Page<SysSettingVo>> getPage(SysSettingQuery query) {
        if (BlankUtil.isEmpty(query.getPageNum())) query.setPageNum(1);
        if (BlankUtil.isEmpty(query.getPageSize())) query.setPageSize(10);
        return sysSettingService.getPage(query);
    }

    @ApiOperation(value = "根据ID查询单条记录")
    @GetMapping(value = "/{sysSettingId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysSettingId", value = "系统设置id", dataType = "Long", required = true)
    })
    public CommonResult<SysSetting> getOne(@PathVariable Long sysSettingId) {
        return sysSettingService.getOne(sysSettingId);
    }

    @ApiOperation("添加或修改设置")
    @PostMapping(value = "/saveOrUpdate")
    public CommonResult<Boolean> saveOrUpdate(@RequestBody SysSettingParam param) {
        Long pcUserId = PcUserUtil.getCurrentUser().getPcUserId();
        return sysSettingService.addOrUpdate(param, pcUserId);
    }

    @ApiOperation("删除单条设置记录")
    @DeleteMapping(value = "/{sysSettingId:^\\d{19}$}")
    public CommonResult<Boolean> delete(@PathVariable Long sysSettingId) {
        Long pcUserId = PcUserUtil.getCurrentUser().getPcUserId();
        return sysSettingService.deleteOne(sysSettingId, pcUserId);
    }

    @ApiOperation("查询所有分组")
    @GetMapping(value = "/getGroupName")
    public CommonResult<List<String>> getGroupName() {
        return CommonResult.success(sysSettingService.getGroupName());
    }

    @ApiOperation("判断设置类型是否已存在")
    @GetMapping(value = "/isExist/{settingType}")
    @ApiImplicitParam(name = "settingType", value = "系统设置类型", dataType = "int", required = true)
    public CommonResult<Boolean> isExist(@PathVariable @EnumContains(enumClass = SettingTypeEnum.class) Integer settingType) {
        Boolean exist = sysSettingService.isExist(EnumTool.getEnum(SettingTypeEnum.class, settingType));
        String msg = "设置类型【" + settingType + "】";
        return exist ? CommonResult.failed(msg + "已存在", Boolean.FALSE) : CommonResult.success(msg + "不存在，允许添加", Boolean.TRUE);
    }

    @ApiOperation("获取当前最大的设置类型")
    @GetMapping(value = "/getMaxSettingType")
    public CommonResult<Integer> getMaxSettingType() {
        Integer maxSettingType = sysSettingService.getMaxSettingType();
        return CommonResult.success(maxSettingType);
    }

}
