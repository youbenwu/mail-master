package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.entity.PcRole;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcRoleParam;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.admin.vo.RoleOwnMenuVO;
import com.ys.mail.service.PcRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 后台角色表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@RestController
@RequestMapping("/pc/role")
@Validated
@Api(tags = "后台角色管理")
public class PcRoleController {

    @Autowired
    private PcRoleService pcRoleService;

    @ApiOperation("后台角色管理-列表-DT")
    @PostMapping(value = "/list")
    public CommonResult<Page<PcRole>> list(@Validated @RequestBody Query query) {
        QueryWrapper<PcRole> wrapper = new QueryWrapper<PcRole>().orderByDesc("role_id");
        Page<PcRole> page = new Page<>(query.getPageNum(), query.getPageSize());
        return CommonResult.success(pcRoleService.page(page, wrapper));
    }

    @ApiOperation("为角色授予菜单-DT")
    @PostMapping(value = "/grantMenu/{roleId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "menuIds", value = "集合菜单id", dataType = "Long", allowMultiple = true, required = true)
    })
    public CommonResult<Integer> grantMenu(@PathVariable Long roleId,
                                           @RequestParam(value = "menuIds") @NotEmpty List<Long> menuIds) {
        int count = pcRoleService.grantMenu(roleId, menuIds);
        return count > NumberUtils.INTEGER_ZERO ? CommonResult.success("success", count) : CommonResult.failed("error");
    }

    @ApiOperation("新增或修改角色-DT")
    @PostMapping(value = "/create")
    public CommonResult<Boolean> create(@Validated @RequestBody PcRoleParam param) {
        boolean b = pcRoleService.createPcRole(param);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("后台角色删除-DT")
    @DeleteMapping(value = "/delete/{roleId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "Long", required = true)
    })
    public CommonResult<Integer> delete(@PathVariable Long roleId) {
        int count = pcRoleService.delRole(roleId);
        return count > NumberUtils.INTEGER_ZERO ? CommonResult.success(count) : CommonResult.failed("删除失败");
    }

    @ApiOperation("角色详情-DT")
    @GetMapping(value = "/getInfo/{roleId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "Long", required = true)
    })
    @LocalLockAnn(key = "PcRoleGetInfo:arg[0]")
    public CommonResult<PcRole> getInfo(@PathVariable Long roleId) {
        PcRole result = pcRoleService.getById(roleId);
        return CommonResult.success(result);
    }

    /**
     * 获取角色拥有的菜单
     */
    @ApiOperation("获取角色拥有的菜单-DT")
    @GetMapping("/{roleId:^\\d{19}$}/ownMenu")
    @ResponseBody
    public CommonResult<RoleOwnMenuVO> getRoleOwnMenu(@PathVariable("roleId") Long roleId) {
        RoleOwnMenuVO vo = pcRoleService.getMenusByRoleId(roleId);
        return CommonResult.success(vo);
    }

}
