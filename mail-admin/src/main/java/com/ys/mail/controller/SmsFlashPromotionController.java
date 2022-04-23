package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcFlashPromotionParam;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.service.SmsFlashPromotionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.text.ParseException;

/**
 * <p>
 * 限时购表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
@RestController
@RequestMapping("/flash/promotion")
@Validated
@Api(tags = "后台限时购管理")
public class SmsFlashPromotionController {

    @Autowired
    private SmsFlashPromotionService flashPromotionService;

    @ApiOperation("限时购管理-列表-DT")
    @PostMapping(value = "/list")
    public CommonResult<Page<SmsFlashPromotion>> list(@Validated @RequestBody Query query) {
        QueryWrapper<SmsFlashPromotion> wrapper = new QueryWrapper<SmsFlashPromotion>().orderByDesc("flash_promotion_id");
        Page<SmsFlashPromotion> page = new Page<>(query.getPageNum(), query.getPageSize());
        return CommonResult.success(flashPromotionService.page(page, wrapper));
    }

    @ApiOperation("限时购详情-DT")
    @GetMapping(value = "/getInfo/{flashPromotionId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionId", value = "限时购id", dataType = "Long", required = true)
    })
    public CommonResult<SmsFlashPromotion> getInfo(@PathVariable Long flashPromotionId) {
        SmsFlashPromotion result = flashPromotionService.getById(flashPromotionId);
        return CommonResult.success(result);
    }

    @ApiOperation("限时购删除-DT")
    @DeleteMapping(value = "/delete/{flashPromotionId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionId", value = "限时购id", dataType = "Long", required = true)
    })
    public CommonResult<Boolean> delete(@PathVariable Long flashPromotionId) throws ParseException {
        // 限时购删除,必须把限时购与商品关联的也删除
        boolean b = flashPromotionService.delFlashPromotion(flashPromotionId);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("新增或修改限时购-DT")
    @PostMapping(value = "/create")
    public CommonResult<Boolean> create(@Validated @RequestBody PcFlashPromotionParam param) throws ParseException {
        boolean b = flashPromotionService.createFlashPromotion(param);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("发布秒杀商品")
    @PostMapping(value = "/publicFlashProduct")
    public CommonResult<Boolean> publicFlashProduct(@Validated @RequestBody SmsFlashPromotionProduct smsFlashPromotionProduct) {
        boolean b = flashPromotionService.publicFlashProduct(smsFlashPromotionProduct);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("限时购是否展示于首页")
    @PostMapping(value = "/updateHome/{flashPromotionId:^\\d{19}$}")
    public CommonResult<Boolean> updateHome(@PathVariable Long flashPromotionId,
                                            @RequestParam("homeStatus") @NotNull Boolean homeStatus) {
        return flashPromotionService.updateHome(flashPromotionId, homeStatus);
    }

    @ApiOperation("限时购上架状态")
    @PostMapping(value = "/updatePublish/{flashPromotionId:^\\d{19}$}")
    public CommonResult<Boolean> updatePublish(@PathVariable Long flashPromotionId,
                                               @RequestParam("publishStatus") @NotNull Boolean publishStatus) throws ParseException {
        boolean b = flashPromotionService.updatePublish(flashPromotionId, publishStatus);
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }
}
