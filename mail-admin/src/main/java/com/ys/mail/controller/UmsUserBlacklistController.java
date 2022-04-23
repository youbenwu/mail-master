package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.UmsUserBlacklist;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.UmsUserBlackListParam;
import com.ys.mail.model.admin.query.UserBlackListQuery;
import com.ys.mail.service.UmsUserBlacklistService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.PcUserUtil;
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
 * @since 2022-01-30
 */
@RestController
@Api(tags = "APP用户黑名单管理")
@RequestMapping("/pc/userBlackListManage")
public class UmsUserBlacklistController {

    @Autowired
    private UmsUserBlacklistService umsUserBlacklistService;

    @ApiOperation(value = "黑名单列表查询", notes = "支持分页、多条件查询")
    @GetMapping(value = "/getPage")
    public CommonResult<Page<UmsUserBlacklist>> getPage(@Validated UserBlackListQuery query) {
        if (BlankUtil.isEmpty(query.getPageNum())) query.setPageNum(1);
        if (BlankUtil.isEmpty(query.getPageSize())) query.setPageSize(10);
        return umsUserBlacklistService.getPage(query);
    }

    @ApiOperation(value = "根据ID查询单条记录")
    @GetMapping(value = "/{blId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "blId", value = "黑名单id", dataType = "Long", required = true)
    })
    public CommonResult<UmsUserBlacklist> getOne(@PathVariable Long blId) {
        return umsUserBlacklistService.getOne(blId);
    }

    @ApiOperation("新增或修改黑名单")
    @PostMapping(value = "/saveOrUpdate")
    public CommonResult<Boolean> saveOrUpdate(@Validated @RequestBody UmsUserBlackListParam param) {
        Long pcUserId = PcUserUtil.getCurrentUser().getPcUserId();
        return umsUserBlacklistService.update(param, pcUserId);
    }

    @ApiOperation("删除黑名单记录")
    @DeleteMapping(value = "/delete")
    public CommonResult<Boolean> delete(@RequestParam(name = "blId") Long blId) {
        return umsUserBlacklistService.deleteOne(blId);
    }

}
