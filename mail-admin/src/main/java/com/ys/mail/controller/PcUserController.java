package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.entity.PcUser;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcUserParam;
import com.ys.mail.model.admin.param.PcUserRegisterParam;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.admin.vo.UserLoginVO;
import com.ys.mail.model.admin.vo.UserOwnRoleVO;
import com.ys.mail.service.PcUserService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@Validated
@RestController
@RequestMapping("/pc/user")
@Api(tags = "后台用户管理")
public class PcUserController {

    @Autowired
    private PcUserService pcUserService;

    @ApiOperation("后台用户注册-DT")
    @PostMapping(value = "/register")
    @LocalLockAnn(key = "PcUserRegister:arg[0]", expire = 10)
    public CommonResult<Boolean> register(@Validated @RequestBody PcUserRegisterParam param) {
        return pcUserService.register(param);
    }

    @ApiOperation("后台用户修改-DT")
    @PostMapping(value = "/update")
    @LocalLockAnn(key = "PcUpdate:arg[0]")
    public CommonResult<Boolean> update(@Validated @RequestBody PcUserParam param) {
        return pcUserService.createAndUpdateUser(param);
    }

    @ApiOperation(value = "后台用户名是否已存在", notes = "true -> 不存在，false -> 存在")
    @PostMapping(value = "/isExistsName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名称，账号", dataType = "String", required = true)
    })
    @LocalLockAnn(key = "isExistsName:arg[0]")
    public CommonResult<Boolean> isExistsName(@RequestParam("username") @NotBlank @Size(min = 4) String username) {
        // 如果为空则为true
        return BlankUtil.isEmpty(pcUserService.getUserByUsername(username)) ?
                CommonResult.success("用户名不存在", Boolean.TRUE) :
                CommonResult.success("用户名已存在", Boolean.FALSE);
    }

    @ApiOperation("后台用户登录-DT")
    @PostMapping(value = "/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名称", dataType = "String", required = true),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "String", required = true)
    })
    public CommonResult<UserLoginVO> login(@RequestParam("username") @NotBlank @Size(min = 4) String username,
                                           @RequestParam("password") @NotBlank @Size(min = 6) String password) {
        UserLoginVO result = pcUserService.userLogin(username, password);
        return CommonResult.success(result);
    }

    @ApiOperation("退出登录-DT")
    @PostMapping(value = "/logout")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        boolean b = pcUserService.logout(request);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("后台用户管理-列表-DT")
    @PostMapping(value = "/list")
    public CommonResult<Page<PcUser>> list(@Validated @RequestBody Query query) {
        QueryWrapper<PcUser> wrapper = new QueryWrapper<PcUser>().orderByDesc("pc_user_id");
        Page<PcUser> page = new Page<>(query.getPageNum(), query.getPageSize());
        return CommonResult.success(pcUserService.page(page, wrapper));
    }

    @ApiOperation("为用户授予角色-DT")
    @PostMapping(value = "/grantRole/{pcUserId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pcUserId", value = "用户id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "roleIds", value = "集合角色id", dataType = "Long", allowMultiple = true, required = true)
    })
    @LocalLockAnn(key = "PcUserGrantRole:arg[0]")
    public CommonResult<Integer> grantRole(@PathVariable Long pcUserId,
                                           @RequestParam(value = "roleIds") @NotEmpty List<Long> roleIds) {
        int count = pcUserService.grantRole(pcUserId, roleIds);
        return count > NumberUtils.INTEGER_ZERO ? CommonResult.success("success", count) : CommonResult.failed("error");
    }

    @ApiOperation("后台用户删除-DT")
    @DeleteMapping(value = "/delete/{pcUserId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pcUserId", value = "用户id", dataType = "Long", required = true)
    })
    @LocalLockAnn(key = "PcUserDelete:arg[0]")
    public CommonResult<Integer> delete(@PathVariable Long pcUserId) {
        int count = pcUserService.delPcUser(pcUserId);
        return count > NumberUtils.INTEGER_ZERO ? CommonResult.success("success", count) : CommonResult.failed("error");
    }

    @ApiOperation("后台用户详情-DT")
    @GetMapping(value = "/getInfo/{pcUserId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pcUserId", value = "用户id", dataType = "Long", required = true)
    })
    public CommonResult<PcUser> getInfo(@PathVariable Long pcUserId) {
        PcUser result = pcUserService.getById(pcUserId);
        return CommonResult.success(result);
    }

    /**
     * 获取用户拥有的角色
     */
    @ApiOperation("获取用户拥有的角色-DT")
    @GetMapping("/{pcUserId:^\\d{19}$}/ownRole")
    @ResponseBody
    public CommonResult<UserOwnRoleVO> getUserOwnRole(@PathVariable("pcUserId") Long pcUserId) {
        UserOwnRoleVO vo = pcUserService.getUserOwnRole(pcUserId);
        return CommonResult.success(vo);
    }
}
