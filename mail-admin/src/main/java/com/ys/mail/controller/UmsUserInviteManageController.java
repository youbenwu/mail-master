package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.UmsUserInvite;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.UmsUserInviteManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * app用户邀请管理 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */
@RestController
@RequestMapping("/pc/userManage")
@Validated
@Api(tags = "用户邀请管理")
public class UmsUserInviteManageController {

    @Autowired
    private UmsUserInviteManageService umsUserInviteManageService;

    @ApiOperation("用户邀请管理修改")
    @PutMapping(value = "/updateUmsUserInvite")
    public CommonResult<Boolean> updateUmsUserInvite(@RequestBody UmsUserInvite umsUserInvite) {
        Boolean result = umsUserInviteManageService.updateById(umsUserInvite);
        return CommonResult.success(result);
    }

    @ApiOperation("用户邀请管理查询")
    @GetMapping(value = "getUmsUserInvite")
    public CommonResult<Page<UmsUserInvite>> getUmsUserInvite(@RequestParam(name = "userName", required = false) String userName, @RequestParam(name = "parendName", required = false) String parendName, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        Page<UmsUserInvite> result = umsUserInviteManageService.getPcInviteUser(userName, parendName);
        return CommonResult.success(result);
    }

    @ApiOperation("用户邀请管理删除")
    @DeleteMapping(value = "deleteUmsUserInvite")
    public CommonResult<Boolean> deleteUmsUserInvite(@RequestParam(name = "userId") Long userId) {
        Boolean result = umsUserInviteManageService.removeById(userId);
        return CommonResult.success(result);
    }
}
