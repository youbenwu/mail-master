package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.entity.UmsRealName;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.UmsRealNameService;
import com.ys.mail.util.CardUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 实名认证表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-12-17
 */
@RestController
@RequestMapping("/ums-real-name")
@Api(tags = "实名认真管理")
public class UmsRealNameController {

    @Autowired
    private UmsRealNameService umsRealNameService;

    @ApiOperation("实名认证添加")
    @PutMapping(value = "/addUmsRealName")
    public CommonResult<Boolean> add(@Validated @RequestBody UmsRealName umsRealName) {
        Boolean result;
        Long id = umsRealName.getRealNameId();
        umsRealName.setRealNameId(id.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : id);
        umsRealName.setUserId(UserUtil.getCurrentUser().getUserId());
        if (CardUtil.isIdentityCard(umsRealName.getCardNumber())) {
            QueryWrapper<UmsRealName> qw = new QueryWrapper<>();
            qw.eq("user_id", UserUtil.getCurrentUser().getUserId());
            int count = umsRealNameService.count(qw);
            if (count > 0) {
                return CommonResult.failed("你已实名认证，无需重复提交");
            } else {
                result = umsRealNameService.save(umsRealName);
            }
        } else {
            return CommonResult.failed("请输入正确身份证号码");
        }
        return CommonResult.success(result);
    }

    @ApiOperation("实名认证修改")
    @PutMapping(value = "/updateUmsRealName")
    public CommonResult<Boolean> updateUmsRealName(@Validated @RequestBody UmsRealName umsRealName) {
        Boolean result;
        if (CardUtil.isIdentityCard(umsRealName.getCardNumber())) {
            result = umsRealNameService.updateById(umsRealName);
        } else {
            return CommonResult.failed("请输入正确身份证号码");
        }
        return CommonResult.success(result);
    }

    @ApiOperation("获取实名认证信息")
    @GetMapping(value = "/getUmsRealName")
    public CommonResult<UmsRealName> getUmsRealName() {
        QueryWrapper<UmsRealName> qw = new QueryWrapper<>();
        qw.eq("user_id", UserUtil.getCurrentUser().getUserId());
        UmsRealName umsRealName = umsRealNameService.getOne(qw);
        return CommonResult.success(umsRealName);
    }

}
