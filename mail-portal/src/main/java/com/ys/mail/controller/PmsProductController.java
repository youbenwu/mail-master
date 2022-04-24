package com.ys.mail.controller;


import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.annotation.ProductLog;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.entity.PmsSkuStock;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.BuyProductDTO;
import com.ys.mail.model.dto.ProductCollectDTO;
import com.ys.mail.model.dto.ProductInfoDTO;
import com.ys.mail.model.param.BathGenerateOrderParam;
import com.ys.mail.model.param.ConGenerateOrderParam;
import com.ys.mail.service.PmsProductService;
import com.ys.mail.service.PmsSkuStockService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.util.List;

/**
 * <p>
 * 商品信息表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/product")
@Validated
@Api(tags = "商品管理app端")
public class PmsProductController {

    @Autowired
    private PmsProductService productService;
    @Autowired
    private PmsSkuStockService skuStockService;

    @ProductLog("用户浏览商品")
    @ApiOperation("商品详情页-DT")
    @GetMapping(value = "/getProductInfo/{productId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "flag", value = "传值null或者false不会查出会员价,传true就有会员价,会员专区传", dataType = "Boolean")
    })
    public CommonResult<ProductInfoDTO> getProductInfo(@PathVariable Long productId,
                                                       @RequestParam(value = "flag", required = false) Boolean flag) {
        // 商品详情页
        ProductInfoDTO result = productService.getProductInfo(productId, flag);
        return CommonResult.success(result);
    }


    @ApiOperation("商品收藏和取消收藏-DT")
    @PostMapping(value = "/collectProduct")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", dataType = "String", required = true),
            @ApiImplicitParam(name = "pdtCollectId", value = "收藏id,收藏时候传0,取消收藏传id过来", dataType = "Long", required = true)
    })
    @LocalLockAnn(key = "collectProduct:arg[0]")
    public CommonResult<String> collectProduct(@RequestParam("productId") @NotBlank @Pattern(regexp = "^\\d{19}$") String productId,
                                               @RequestParam("pdtCollectId") @NotNull @Pattern(regexp = "^0|^\\d{19}$") String pdtCollectId) {
        // 由前端判断,根据样式修改
        return productService.collectProduct(Long.valueOf(productId), Long.valueOf(pdtCollectId));
    }

    @ApiOperation("商品收藏列表-DT")
    @GetMapping(value = "/getAllCollect/{pdtCollectId:^0|^\\d{19}$}")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "pdtCollectId", value = "翻页id,传0首次,每次传最后一个", dataType = "Long", required = true)
    })
    public CommonResult<List<ProductCollectDTO>> getAllCollectProduct(@PathVariable Long pdtCollectId) {

        List<ProductCollectDTO> result = productService.getAllCollectProduct(pdtCollectId);
        return CommonResult.success(result);
    }

    @ApiOperation("批量删除收藏的商品-DT")
    @PostMapping(value = "/batchCollectDel")
    public CommonResult<Boolean> batchCollectDel(@RequestParam("ids") @NotEmpty List<Long> ids) {
        // 购物车列表删除商品
        return productService.batchCollectDel(ids);
    }

    @ApiOperation("单个查询sku属性")
    @GetMapping(value = "/getSku/{productId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "spData", value = "商品属性拼接,[{\\\"key\\\":\\\"颜色\\\",\\\"value\\\":\\\"黑色\\\"},{\\\"key\\\":\\\"容量\\\",\\\"value\\\":\\\"32G\\\"}],前端必须按照这种格式来", dataType = "String", required = true),
    })
    public CommonResult<PmsSkuStock> getSku(@PathVariable Long productId,
                                            @RequestParam("spData") String spData) {
        PmsSkuStock skuStock = skuStockService.getSku(productId, spData);
        return BlankUtil.isEmpty(skuStock) ? CommonResult.failed(BusinessErrorCode.GOODS_NOT_EXIST) : CommonResult.success(skuStock);
    }

    @ApiOperation("商品立即购买页面-DT")
    @PostMapping(value = "/getBuy/{skuStockId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuStockId", value = "商品skuID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "quantity", value = "数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "flag", value = "标识", dataType = "boolean"),
            @ApiImplicitParam(name = "addressId", value = "地址ID", dataType = "Long")
    })
    public CommonResult<BuyProductDTO> getBuyProduct(@PathVariable Long skuStockId,
                                                     @RequestParam(value = "quantity", defaultValue = "1") @Min(value = 1) Integer quantity,
                                                     @RequestParam(value = "flag", required = false) Boolean flag,
                                                     @RequestParam(value = "addressId", required = false)
                                                     @BlankOrPattern(regexp = "^\\d{19}$") Long addressId) {
        // 当前的这个商品id,sku_id,数量, 获取购买商品的基本属性,加上会员价,由web前端传入是否显示会员价给展示
        BuyProductDTO result = productService.getBuyProduct(skuStockId, quantity, flag, addressId);
        return BlankUtil.isEmpty(result) ? CommonResult.failed(BusinessErrorCode.GOODS_NOT_EXIST) : CommonResult.success(result);
    }

    @ApiOperation("礼品确认页面")
    @PostMapping(value = "/getGift/{productId:^\\d{19}$}")
    public CommonResult<BuyProductDTO> getBuyProduct(@PathVariable Long productId) {
        // 根据商品id获取购买商品的基本属性
        BuyProductDTO result = productService.getBuyProduct(productId);
        return BlankUtil.isEmpty(result) ? CommonResult.failed(BusinessErrorCode.GOODS_NOT_EXIST) : CommonResult.success(result);
    }


    @ApiOperation("普通生成订单-DT")
    @PostMapping(value = "/generateOrder")
    @LocalLockAnn(key = "generateOrder:arg[0]")
    public CommonResult<GenerateOrderBO> generateOrder(@Validated @RequestBody ConGenerateOrderParam param) {
        // 生成订单
        return productService.generateOrder(param);
    }

    @ApiOperation("页面商品精品推荐-DT")
    @PostMapping(value = "/getProductOfHandpickRecommend")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品ID，用于分页"),
            @ApiImplicitParam(name = "pageSize", value = "分页数量，默认10条，最大50条")
    })
    public CommonResult<List<PmsProduct>> getProductOfHandpickRecommend(@RequestParam(name = "productId", defaultValue = "0") Long productId, @RequestParam(name = "pageSize", defaultValue = "10") @Range(min = 1, max = 50, message = "分页大小范围为1-50条") String pageSize) {
        List<PmsProduct> result = productService.getProductOfHandpickRecommend(productId, Integer.valueOf(pageSize));
        return CommonResult.success(result);
    }

    @ApiOperation("购物车商品生成订单")
    @PostMapping(value = "/bathGenerateOrder")
    @LocalLockAnn(key = "bathGenerateOrder:arg[0]")
    public CommonResult<Boolean> bathGenerateOrder(@Validated @RequestBody BathGenerateOrderParam param) {
        return productService.bathGenerateOrder(param);
    }

    @ApiOperation("查看礼品")
    @GetMapping(value = "/getGift")
    public CommonResult<List<PmsProduct>> getGift() {
        return CommonResult.success(productService.getGift());
    }

    /**
     * 合伙人产品库存
     */
    @PostMapping(value = "/getPartnerProduct")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品ID，用于分页"),
            @ApiImplicitParam(name = "pageSize", value = "分页数量，默认10条，最大50条")
    })
    public CommonResult<List<PmsProduct>> partnerProduct(@RequestParam(name = "productId", defaultValue = "0") Long productId, @RequestParam(name = "pageSize", defaultValue = "10") @Range(min = 1, max = 50, message = "分页大小范围为1-50条") String pageSize) {
        List<PmsProduct> result = productService.partnerProduct(productId, Integer.valueOf(pageSize));
        return CommonResult.success(result);
    }
}
