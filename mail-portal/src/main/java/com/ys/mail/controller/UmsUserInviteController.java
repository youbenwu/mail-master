package com.ys.mail.controller;


import com.ys.mail.annotation.ApiBlock;
import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.vo.UserOrderVO;
import com.ys.mail.service.OmsOrderService;
import com.ys.mail.service.UmsUserInviteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * <p>
 * 用户邀请信息表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@RestController
@RequestMapping("/ums-user-invite")
@Validated
@Api(tags = "用户邀请")
public class UmsUserInviteController {

    @Autowired
    private UmsUserInviteService umsUserInviteService;
    @Autowired
    private OmsOrderService omsOrderService;

    @ApiBlock
    @ApiOperation("查看用户邀请二维码")
    @PostMapping(value = "/getUserInviteQrCode")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公司类型:0->大尾狐,1->呼啦兔，默认0", name = "type")
    })
    public CommonResult<String> getUserInviteQrCode(@RequestParam(value = "type", defaultValue = "0") String type) throws Exception {
        return umsUserInviteService.getUserInviteQrCode(type);
    }

    @ApiBlock
    @ApiOperation("扫描二维码升级高级会员")
    @PostMapping(value = "/updateUserRole")
    public CommonResult<Boolean> updateUserRole(@RequestParam("parentId") Long parentId) {
        return umsUserInviteService.updateUserRole(parentId);
    }

    @ApiOperation("根据用户id查询相关的秒杀订单")
    @GetMapping("/getUserOrderList")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "目标用户id", name = "userId", required = true),
            @ApiImplicitParam(value = "翻页订单id，可选", name = "orderId"),
            @ApiImplicitParam(value = "查询的条数(默认是5，范围：1~50)", name = "pageSize", dataType = "int")
    })
    public CommonResult<List<UserOrderVO>> getUserOrderList(@RequestParam(value = "userId") @Pattern(regexp = "^\\d{19}$") String userId,
                                                            @RequestParam(value = "orderId", required = false) @BlankOrPattern(regexp = "^\\d{19}$") String orderId,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = "5") @Min(value = 1) @Max(value = 50) Integer pageSize) {
        List<UserOrderVO> list = omsOrderService.getUserOrderList(userId, orderId, pageSize);
        return CommonResult.success(list);
    }
}
