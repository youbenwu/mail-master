package com.ys.mail.controller;


import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.GroupBuyDTO;
import com.ys.mail.model.dto.GroupBuyInfoDTO;
import com.ys.mail.service.GroupBuyService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 团购 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-13
 */
@RestController
@RequestMapping("/group/buy")
@Validated
@Api(tags = "拼团管理")
public class GroupBuyController {

    @Autowired
    private GroupBuyService groupBuyService;

    @ApiOperation("首页全部拼团-DT")
    @GetMapping(value = "/getAllGroupBuy/{pageNum:^\\d{1,}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "二十条一翻页,首次传0,第二次20,能后类加", dataType = "Long", required = true)
    })
    public CommonResult<List<GroupBuyDTO>> getAllGroupBuy(@PathVariable Long pageNum) {
        // 查询首页显示的全部可以拼团的商品,定义二十条一翻页
        List<GroupBuyDTO> result = groupBuyService.getAllGroupBuy(pageNum);
        return CommonResult.success(result);
    }


    @ApiOperation("拼团详情页-DT")
    @GetMapping(value = "/info/{productId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", dataType = "Long", required = true)
    })
    public CommonResult<GroupBuyInfoDTO> info(@PathVariable Long productId) {
        // 拼团详情需要哪些,1拼团的列表,拼团的列表有就显示,没有就隐藏
        GroupBuyInfoDTO result = groupBuyService.groupBuyInfo(productId);
        return BlankUtil.isEmpty(result) ? CommonResult.failed(BusinessErrorCode.GOODS_NOT_EXIST) : CommonResult.success(result);
    }
}
