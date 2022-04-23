package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.dto.PcFlashPdtDTO;
import com.ys.mail.model.admin.dto.PcFlashPromotionProductDTO;
import com.ys.mail.model.admin.dto.SessionOrPdtDTO;
import com.ys.mail.model.admin.param.PcFlashPromotionProductParam;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.service.SmsFlashPromotionProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品限时购与商品关系表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
@RestController
@RequestMapping("/flash/promotion/product")
@Validated
@Api(tags = "后台限时购商品管理")
public class SmsFlashPromotionProductController {

    @Autowired
    private SmsFlashPromotionProductService flashPromotionProductService;

    @ApiOperation("限时购商品管理-列表-DT")
    @PostMapping(value = "/list")
    public CommonResult<Page<PcFlashPdtDTO>> list(@Validated @RequestBody Query query) {
        return CommonResult.success(flashPromotionProductService.list(query));
    }

    @ApiOperation("限时购商品删除-DT")
    @DeleteMapping(value = "/delete/{flashPromotionPdtId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionPdtId", value = "主键id", dataType = "Long", required = true)
    })
    public CommonResult<Boolean> delete(@PathVariable Long flashPromotionPdtId) {
        boolean b = flashPromotionProductService.delete(flashPromotionPdtId);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("限时购商品详情-DT")
    @GetMapping(value = "/getInfo/{flashPromotionPdtId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionPdtId", value = "主键id", dataType = "Long", required = true)
    })
    public CommonResult<PcFlashPromotionProductDTO> getInfo(@PathVariable Long flashPromotionPdtId) {
        PcFlashPromotionProductDTO result = flashPromotionProductService.getInfoById(flashPromotionPdtId);
        return CommonResult.success(result);
    }

    @ApiOperation("新增或修改限时购商品-DT")
    @PostMapping(value = "/create")
    public CommonResult<Boolean> create(@Validated @RequestBody PcFlashPromotionProductParam param) {
        boolean b = flashPromotionProductService.create(param);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("最新场次和商品-DT")
    @GetMapping(value = "/getSessionOrPdt")
    public CommonResult<SessionOrPdtDTO> getSessionOrPdt() {
        // 最新场次id,name,商品id,商品Name
        return CommonResult.success(flashPromotionProductService.getSessionOrPdt());
    }
}
