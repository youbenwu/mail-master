package com.ys.mail.controller;

import com.ys.mail.entity.PmsProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.ProductParam;
import com.ys.mail.model.po.ProductPO;
import com.ys.mail.model.vo.HomePageVO;
import com.ys.mail.service.PmsProductService;
import com.ys.mail.service.SmsHomeAdvertiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-25 16:08
 */
@RestController
@RequestMapping("/index")
@Validated
@Api(tags = "APP首页管理")
public class IndexController {

    @Autowired
    private SmsHomeAdvertiseService advertiseService;
    @Autowired
    private PmsProductService productService;

    @ApiOperation("首页-轮播图,限时秒杀,拼团购-DT,其他设置")
    @GetMapping(value = "/homePage/{cpyType:^0|^1}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cpyType", value = "0->轻创营,1->卖乐吧", dataType = "Byte", required = true)
    })
    public CommonResult<HomePageVO> homePage(@PathVariable Byte cpyType) {
        HomePageVO vo = advertiseService.homePage(cpyType);
        return CommonResult.success(vo);
    }

    @ApiOperation("精选,精致,穿搭,生活-DT")
    @GetMapping(value = "/getHome/{productId:^0|^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id首次传0,每次传最后一个id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "homeProductType", value = "1精选,2精致,3穿搭,4生活", dataType = "Integer", required = true)
    })
    public CommonResult<List<PmsProduct>> getHomeProductType(@PathVariable Long productId,
                                                             @RequestParam("homeProductType") @Range(min = 1, max = 4) Integer homeProductType) {
        // 然后根据key进行查询,精选,精致,穿搭,生活,从一张表就是加入三个状态,另外一种是建三张表来存储
        List<PmsProduct> result = productService.getHomeProductType(productId, homeProductType);
        return CommonResult.success(result);
    }

    @ApiOperation("自营列表-DT")
    @GetMapping("/getAll/{productId:^0|^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id,翻页,首次传进来为0,翻页每次传最后一个id", dataType = "Long", required = true)
    })
    public CommonResult<List<PmsProduct>> getAllProduct(@PathVariable Long productId) {
        List<PmsProduct> products = productService.getAllProduct(productId);
        return CommonResult.success(products);
    }

    @ApiOperation("为你精选,猜你喜欢-DT")
    @GetMapping(value = "/getHandpick/{productId:^0|^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id,翻页,首次传进来为0,翻页每次传最后一个id,翻页是20条一翻", dataType = "Long", required = true),
            @ApiImplicitParam(name = "pdtCgyId", value = "服饰:1458319099959578624,母婴:1458319174404280320," +
                    "珠宝:1458319250153410560,家电:1458319278142001152", dataType = "Long", required = true)
    })
    public CommonResult<List<PmsProduct>> getHandpickProduct(@PathVariable Long productId,
                                                             @RequestParam("pdtCgyId") @NotBlank @Pattern(regexp = "^\\d{19}$") String pdtCgyId) {

        List<PmsProduct> products = productService.getHandpickProduct(productId, pdtCgyId);
        return CommonResult.success(products);
    }

    @ApiOperation("首页服饰,珠宝等进入的大牌精选等-DT")
    @GetMapping(value = "/getProductPick/{pdtCgyId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pdtCgyId", value = "服饰:1458319099959578624,家电:1458319174404280320," +
                    "珠宝:1458319250153410560,家电:1458319278142001152", dataType = "Long", required = true)
    })
    public CommonResult<List<PmsProduct>> getProductPick(@PathVariable Long pdtCgyId) {
        List<PmsProduct> result = productService.getProductPick(pdtCgyId);
        return CommonResult.success(result);
    }

    @ApiOperation("首页、会员专享")
    @GetMapping(value = "/searchAllPdtType")
    public CommonResult<List<ProductPO>> searchAllPdtType(@Validated ProductParam param) {
        List<ProductPO> result = productService.searchAllPdtType(param);
        return CommonResult.success(result);
    }

}
