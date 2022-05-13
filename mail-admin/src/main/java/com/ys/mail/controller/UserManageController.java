package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.UmsUserQuery;
import com.ys.mail.model.admin.vo.UmsUserBlackListVO;
import com.ys.mail.service.UserManageService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


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
@Api(tags = "APP用户管理")
public class UserManageController {

    @Autowired
    private UserManageService userManageService;

    @ApiOperation("移动平台用户添加")
    @PutMapping(value = "/addUmsUser")
    public CommonResult<Boolean> addUmsUser(@RequestBody UmsUser umsUser) {
        Long id = umsUser.getUserId();
        umsUser.setUserId(id.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : id);
        Boolean result = userManageService.saveOrUpdate(umsUser);
        return CommonResult.success(result);
    }

    @ApiOperation("移动平台用户查询")
    @GetMapping(value = "/getUmsUser")
    public CommonResult<IPage<UmsUserBlackListVO>> getUmsUser(UmsUserQuery query) {
        if (BlankUtil.isEmpty(query.getPageNum())) query.setPageNum(1);
        if (BlankUtil.isEmpty(query.getPageSize())) query.setPageSize(10);
        return userManageService.getPage(query);
    }

    @ApiOperation("移动平台用户删除")
    @DeleteMapping(value = "/delete")
    public CommonResult<Boolean> delete(@RequestParam(name = "brandId") Long brandId) {
        Boolean result = userManageService.removeById(brandId);
        return CommonResult.success(result);
    }

    @PostMapping(value = "/exportExcel")
    @ApiOperation(value = "导出平台用户汇总数据", notes = "该接口为导出全量用户数据，筛选条件无效")
    @ApiImplicitParam(name = "condition", value = "0->默认为简单版，1->是否为详细版", dataType = "int")
    public void exportExcel(@RequestParam(name = "condition", defaultValue = "0") boolean condition, HttpServletResponse response) {
        userManageService.exportExcel(condition, response);
    }

    @PostMapping(value = "/exportExcel/{userId:^\\d{19}$}")
    @ApiOperation(value = "导出个人明细数据", notes = "该接口为仅导出单个用户的明细数据")
    @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true)
    public void exportUserExcel(@PathVariable("userId") Long userId, HttpServletResponse response) {
        userManageService.exportUserDetailsExcel(userId, response);
    }

    @PostMapping(value = "/resetPayPassword/{userId:^\\d{19}$}")
    @ApiOperation(value = "重置用户的支付密码", notes = "默认密码为：123456")
    public CommonResult<Boolean> resetPayPassword(@PathVariable("userId") Long userId) {
        boolean result = userManageService.resetPayPassword(userId);
        return result ? CommonResult.success(true) : CommonResult.failed(false);
    }

}
