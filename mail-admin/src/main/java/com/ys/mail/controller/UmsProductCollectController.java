package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.dto.UmsProductCollectDto;
import com.ys.mail.service.UmsProductCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户收藏产品中间表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-12-07
 */
@RestController
@RequestMapping("/pc/product/collect")
@Validated
@Api(tags = "用户收藏管理")
public class UmsProductCollectController {

    @Autowired
    private UmsProductCollectService umsProductCollectService;

    @ApiOperation("用户收藏查询")
    @GetMapping("get")
    public CommonResult<Page<UmsProductCollectDto>> getUmsProductCollectList(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        Page<UmsProductCollectDto> page = new Page<>(pageNum, pageSize);
        Page<UmsProductCollectDto> result = umsProductCollectService.getUmsProductCollectList(page);
        return CommonResult.success(result);
    }

}
