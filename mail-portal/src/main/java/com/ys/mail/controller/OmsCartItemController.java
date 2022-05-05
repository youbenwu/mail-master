package com.ys.mail.controller;


import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.BatchBuyProductDTO;
import com.ys.mail.model.param.CreateOrderParam;
import com.ys.mail.service.OmsCartItemService;
import com.ys.mail.service.PmsProductService;
import com.ys.mail.service.PmsSkuStockService;
import com.ys.mail.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 购物车表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-16
 */
@RestController
@RequestMapping("/cart/item")
@Validated
@Api(tags = "购物车管理")
public class OmsCartItemController {

    @Autowired
    private OmsCartItemService cartItemService;

    @ApiOperation("购物车列表删除商品-已修改")
    @PostMapping(value = "/batchDel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "购物车主键id", dataType = "Long", required = true,allowMultiple = true)
    })
    public CommonResult<Boolean> batchDel(@RequestParam("ids") @NotEmpty List<Long> ids) {
        boolean result = cartItemService.removeByIds(ids);
        return result ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }

    @ApiOperation("添加商品到购物车")
    @PostMapping(value = "/add/{skuId:^\\d{19}$}")
    public CommonResult<Boolean> add(@PathVariable Long skuId,
                                     @RequestParam(value = "quantity", defaultValue = "1") Integer quantity) {
        return cartItemService.add(skuId, quantity);
    }

    @ApiOperation("获取单个用户的购物车列表")
    @GetMapping(value = "/list")
    public CommonResult<List<OmsCartItem>> list() {
        return CommonResult.success(cartItemService.list(UserUtil.getCurrentUser().getUserId()));
    }

    @ApiOperation("从购物车批量添加商品到订单-已修改")
    @PostMapping(value = "/batchProduct")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "购物车主键id", dataType = "Long", required = true,allowMultiple = true)
    })
    public CommonResult<BatchBuyProductDTO> batchProduct(@RequestParam("ids") @NotEmpty List<Long> ids) {
        // TODO 用户地址,商品订单集合,定义一个这样的规则,在会员专区马上购买的就是会员价,如果是加入购物车就是sku的价格,要不然改动非常的大
        return CommonResult.success(cartItemService.batchProduct(ids));
    }

    @ApiOperation("购物车加减数量")
    @PutMapping(value = "/{skuId:^\\d{19}$}/{num:^?[1-9]\\d*$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId", value = "skuId", dataType = "Long", required = true),
            @ApiImplicitParam(name = "num", value = "数量,传几就是设置为几,不能为0和负数", dataType = "Integer", required = true)
    })
    public CommonResult<Boolean> update(@PathVariable Long skuId,
                                        @PathVariable Integer num){
        boolean update = cartItemService.update(skuId, num);
        return update ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }


    @ApiOperation("购物车生成订单")
    @PostMapping(value = "/createOrder")
    @LocalLockAnn(key = "cartItemCreateOrder:arg[0]")
    public CommonResult<GenerateOrderBO> createOrder(@Validated @RequestBody CreateOrderParam param){
        return CommonResult.success(cartItemService.createOrder(param));
    }






//    @ApiOperation("获取购物车信息")
//    @PostMapping(value = "/getCarItem")
//    public CommonResult<List<OmsCartItemDTO>> getCarItem() {
//        // 获取购物车信息
//        List<OmsCartItem> cartItemList = cartItemService.getCarItem();
//        List<OmsCartItemDTO> dto = new ArrayList<>();
//        for (int i = 0; i < cartItemList.size(); i++) {
//            OmsCartItemDTO entity = new OmsCartItemDTO();
//            BeanUtil.copyProperties(cartItemList.get(i), entity);
//            //查询商品
//            PmsProduct pmsProduct = pmsProductService.getById(cartItemList.get(i).getProductId());
//            if (pmsProduct != null) {
//                entity.setProductName(pmsProduct.getProductName());
//                entity.setPic(pmsProduct.getPic());
//                entity.setPublishStatus(pmsProduct.getPublishStatus());
//            }
//            //查询库存
//            PmsSkuStock pmsSkuStock = pmsSkuStockService.getById(cartItemList.get(i).getProductSkuId());
//            if (pmsSkuStock != null) {
//                entity.setSpData(pmsSkuStock.getSpData());
//            }
//            dto.add(entity);
//        }
//        return CommonResult.success("success", dto);
//    }

//    @ApiOperation("获取商品库存信息")
//    @PostMapping(value = "/getPmsSkuStockStatus")
//    public CommonResult<String> getPmsSkuStockStatus(@Validated @RequestParam("skuId") String skuId, @Validated @RequestParam("count") int count) {
//        // 获取购物车信息
//        QueryWrapper<PmsSkuStock> wrapper = new QueryWrapper<>();
//        wrapper.eq("sku_stock_id", skuId).gt("stock", count);
//        int counts = pmsSkuStockService.count(wrapper);
//        if (counts == 0) {
//            return CommonResult.failed("该商品库存不足");
//        } else {
//            return CommonResult.success("库存:" + counts);
//        }
//    }
}
