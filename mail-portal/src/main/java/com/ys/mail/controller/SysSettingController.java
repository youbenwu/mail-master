package com.ys.mail.controller;

import com.ys.mail.entity.SysSetting;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.util.EnumTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-02-16 09:56
 */
@RestController
@Api(tags = "系统设置管理")
@RequestMapping("/sys/settingManage")
public class SysSettingController {

    @Autowired
    private SysSettingService sysService;

    @ApiOperation(value = "获取APP设置列表", notes = "该结果建议别在客户端进行缓存，需要时调用即可")
    @GetMapping(value = "/list")
    public CommonResult<List<SysSetting>> list() {
        return CommonResult.success(this.getGroupName());
    }

    @ApiOperation(value = "获取简单形式的APP设置列表", notes = "方便使用；该结果建议别在客户端进行缓存，需要时调用即可")
    @GetMapping(value = "/simpleList")
    public CommonResult<Map<String, Object>> simpleList() {
        List<SysSetting> list = this.getGroupName();
        Map<String, Object> resultMap = new LinkedHashMap<>();
        // 重新封装结果，简单形式，方便直接使用
        List<SysSetting> collect = list.stream().peek(s -> {
            String key = EnumTool.getValue(SettingTypeEnum.class, s.getSettingType());
            resultMap.put(key, sysService.getSettingValue(s));
        }).collect(Collectors.toList());
        return CommonResult.success(resultMap);
    }

    /**
     * 统一获取组名
     *
     * @return 设置列表
     */
    private List<SysSetting> getGroupName() {
        String groupName = sysService.getGroupNameByType(SettingTypeEnum.two);
        List<SysSetting> availableList = sysService.getSettingByGroupName(groupName);
        return availableList.stream()
                            .filter(s -> !s.getSettingType().equals(SettingTypeEnum.two.getType()))
                            .collect(Collectors.toList());
    }
}
