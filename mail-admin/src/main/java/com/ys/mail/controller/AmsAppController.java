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

    @ApiOperation(value = "APP检测", notes = "当重新上传文件之后，可以手动执行该操作或等待系统自动更新")
    @PostMapping(value = "/check/{id:^\\d{19}$}")
    @LocalLockAnn(key = "check:arg[0]", expire = 60)
    public CommonResult<Boolean> check(@PathVariable Long id) {
        boolean result = amsAppService.check(id);
        return ResultUtil.isOk(result);
    }

    @ApiOperation("重新生成二维码")
    @PostMapping(value = "/reGenQrcode/{id:^\\d{19}$}")
    @LocalLockAnn(key = "reGenQrcode:arg[0]", expire = 60)
    public CommonResult<Boolean> reloadGenQrcode(@PathVariable Long id) {
        boolean result = amsAppService.reloadGenQrcode(id);
        return ResultUtil.isOk(result);
    }

    @ApiOperation(value = "发布应用", notes = "当变更应用信息之后，再执行该操作\n" +
            "另外需谨慎操作，该操作将直接修改系统设置")
    @PostMapping(value = "/release/{id:^\\d{19}$}")
    @LocalLockAnn(key = "release:arg[0]", expire = 60)
    public CommonResult<Boolean> release(@PathVariable Long id) {
        return amsAppService.release(id);
    }

    @ApiOperation(value = "APP刷新预热", notes = "当变更二维码或应用之后，再执行该操作\n" +
            "每天限制配额，刷新为10000条，预热为1000条，执行成功之后预计5~7分钟内生效")
    @PostMapping(value = "/purgeAndWarmUp/{id:^\\d{19}$}")
    @LocalLockAnn(key = "purgeAndWarmUp:arg[0]", expire = 60)
    public CommonResult<Boolean> purgeAndWarmUp(@PathVariable Long id) {
        String result = amsAppService.purgeAndWarmUp(id);
        return ResultUtil.isOk(result, Boolean.TRUE);
    }

}
