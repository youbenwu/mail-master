package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.AmsAppInsertParam;
import com.ys.mail.model.admin.param.AmsAppUpdateParam;
import com.ys.mail.model.admin.query.AppQuery;
import com.ys.mail.model.admin.vo.AmsAppVO;
import com.ys.mail.service.AmsAppService;
import com.ys.mail.util.ResultUtil;
import io.swagger.annotations.Api;
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
 * @since 2022-05-13
 */
@RestController
@Api(tags = "APP应用管理")
@RequestMapping("/applicationManager")
public class AmsAppController {

    @Autowired
    private AmsAppService amsAppService;

    @ApiOperation(value = "查询APP应用列表")
    @GetMapping(value = "/list")
    public CommonResult<IPage<AmsAppVO>> getPage(@Validated AppQuery query) {
        IPage<AmsAppVO> page = amsAppService.getPage(query);
        return CommonResult.success(page);
    }

    @PostMapping(value = "/add")
    @LocalLockAnn(key = "add:arg[0]")
    @ApiOperation(value = "添加APP应用", notes = "需要使用异步文件上传")
    public CommonResult<Boolean> add(@Validated @RequestBody AmsAppInsertParam param) {
        boolean result = amsAppService.add(param);
        return ResultUtil.isOk(result);
    }

    @ApiOperation("更新APP应用")
    @PostMapping(value = "/update")
    @LocalLockAnn(key = "update:arg[0]")
    public CommonResult<Boolean> update(@Validated @RequestBody AmsAppUpdateParam param) {
        boolean result = amsAppService.update(param);
        return ResultUtil.isOk(result);
    }

    @ApiOperation("删除APP应用")
    @DeleteMapping(value = "/{id:^\\d{19}$}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean result = amsAppService.delete(id);
        return ResultUtil.isOk(result);
    }

    @ApiOperation("APP检测")
    @PostMapping(value = "/check/{id:^\\d{19}$}")
    // @LocalLockAnn(key = "check:arg[0]", expire = 60)
    public CommonResult<Boolean> check(@PathVariable Long id) {
        boolean result = amsAppService.check(id);
        return ResultUtil.isOk(result);
    }

    @ApiOperation("重新生成二维码")
    @PostMapping(value = "/reGenQrcode/{id:^\\d{19}$}")
    // @LocalLockAnn(key = "reGenQrcode:arg[0]", expire = 60)
    public CommonResult<Boolean> reloadGenQrcode(@PathVariable Long id) {
        boolean result = amsAppService.reloadGenQrcode(id);
        return ResultUtil.isOk(result);
    }

    @ApiOperation(value = "发布应用", notes = "需谨慎操作")
    @PostMapping(value = "/release/{id:^\\d{19}$}")
    // @LocalLockAnn(key = "release:arg[0]", expire = 60)
    public CommonResult<Boolean> release(@PathVariable Long id) {
        return amsAppService.release(id);
    }

    @ApiOperation(value = "CDN刷新预热", notes = "每天限制配额，刷新为10000条，预热为1000条")
    @PostMapping(value = "/purgeAndWarmUp/{id:^\\d{19}$}")
    // @LocalLockAnn(key = "purgeAndWarmUp:arg[0]", expire = 60)
    public CommonResult<Boolean> purgeAndWarmUp(@PathVariable Long id) {
        amsAppService.purgeAndWarmUp(id);
        return ResultUtil.isOk(Boolean.TRUE);
    }

}
