package com.ys.mail.controller;


import com.ys.mail.model.CommonResult;
import com.ys.mail.model.tree.SysDistrictTree;
import com.ys.mail.service.SysDistrictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 省市区数据字典表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-12-04
 */
@Validated
@RestController
@RequestMapping("/district")
@Api(tags = "地址字典管理")
public class SysDistrictController {

    @Autowired
    private SysDistrictService districtService;

    @ApiOperation("获取字典管理")
    @GetMapping(value = "/trees")
    public CommonResult<List<SysDistrictTree>> trees() {
        List<SysDistrictTree> trees = districtService.trees();
        return CommonResult.success(trees);
    }
}
