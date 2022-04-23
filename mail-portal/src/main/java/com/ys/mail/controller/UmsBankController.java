package com.ys.mail.controller;


import com.ys.mail.entity.UmsBank;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.UmsBankParam;
import com.ys.mail.service.UmsBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户银行卡表,建议后台转1分审核是否成功 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@RestController
@RequestMapping("/bank")
@Api(tags = "用户银行卡管理")
public class UmsBankController {

    @Autowired
    private UmsBankService bankService;

    @ApiOperation("新增银行卡-DT")
    @PostMapping(value = "/saveBank")
    public CommonResult<Boolean> saveBank(@Validated @RequestBody UmsBankParam param) {
        // 新增银行卡
        return bankService.saveBank(param);
    }

    @ApiOperation("银行卡列表-DT")
    @GetMapping(value = "/getAllBank")
    public CommonResult<List<UmsBank>> getAllBank() {
        // 列表
        List<UmsBank> result = bankService.getByUserId();
        return CommonResult.success(result);
    }

    @ApiOperation("解绑银行卡-DT")
    @DeleteMapping(value = "/delete")
    public CommonResult<Boolean> delete() {

        // TODO 解绑银行卡 需要接入第三方支付密码,需要令牌token
        return null;
    }


}
