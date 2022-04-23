package com.ys.mail.controller;


import com.ys.mail.entity.PcMenu;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcMenuParam;
import com.ys.mail.model.admin.tree.PcMenuTree;
import com.ys.mail.service.PcMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@RestController
@RequestMapping("/pc/menu")
@Validated
@Api(tags = "后台菜单管理")
public class PcMenuController {

    @Autowired
    private PcMenuService pcMenuService;

    @ApiOperation("后台菜单管理-树列表-DT")
    @GetMapping(value = "/listTree")
    public CommonResult<List<PcMenuTree>> listTree() {
        List<PcMenuTree> menuTrees = pcMenuService.getAllTreePcMenu();
        return CommonResult.success(menuTrees);
    }

    @ApiOperation("后台菜单新增和修改-DT")
    @PostMapping(value = "/create")
    public CommonResult<Boolean> create(@Validated @RequestBody PcMenuParam param) {
        boolean b = pcMenuService.createPcMenu(param);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("后台菜单删除-DT")
    @DeleteMapping(value = "/delete/{menuId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单id", dataType = "Long", required = true)
    })
    public CommonResult<Integer> delete(@PathVariable Long menuId) {
        int count = pcMenuService.delPcMenu(menuId);
        return count > NumberUtils.INTEGER_ZERO ? CommonResult.success("success", count) : CommonResult.failed("error");
    }

    @ApiOperation("菜单详情-DT")
    @GetMapping(value = "/getInfo/{menuId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单id", dataType = "Long", required = true)
    })
    public CommonResult<PcMenu> getInfo(@PathVariable Long menuId) {
        PcMenu result = pcMenuService.getById(menuId);
        return CommonResult.success(result);
    }

    @ApiOperation("根节点菜单树列表-DT")
    @GetMapping(value = "/rootTree")
    public CommonResult<List<PcMenuTree>> rootTree() {
        List<PcMenuTree> menuTrees = pcMenuService.getAllTreePcMenu();
        return CommonResult.success(menuTrees);
    }
}
