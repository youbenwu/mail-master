package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.QueryParentQuery;
import com.ys.mail.model.vo.UmsPartnerVo;
import com.ys.mail.service.UmsPartnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
@RestController
@RequestMapping("/pc/ums-partner")
@Api(tags = "合伙人管理")
public class UmsPartnerController {

    @Autowired
    private UmsPartnerService umsPartnerService;

    @ApiOperation("合伙人管理-列表-DT")
    @PostMapping(value = "/list")
    public CommonResult<Page<UmsPartner>> list(@Validated @RequestBody QueryParentQuery query) {
        return umsPartnerService.list(query);
    }

    @ApiOperation("合伙人管理-合伙人详情")
    @GetMapping(value = "/getInfo/{partnerId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "partnerId", value = "主键id", dataType = "Long", required = true)
    })
    public CommonResult<UmsPartnerVo> getInfo(@PathVariable Long partnerId) {
        return umsPartnerService.getInfoById(partnerId);
    }

    @ApiOperation("合伙人管理-删除合伙人")
    @DeleteMapping(value = "/delete")
    public CommonResult<Boolean> delete(Long partnerId) {
        return umsPartnerService.delete(partnerId);
    }

}
