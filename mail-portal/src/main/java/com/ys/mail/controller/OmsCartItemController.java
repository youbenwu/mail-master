package com.ys.mail.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.entity.PmsSkuStock;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.OmsCartItemDTO;
import com.ys.mail.model.param.OmsCartItemParam;
import com.ys.mail.service.OmsCartItemService;
import com.ys.mail.service.PmsProductService;
import com.ys.mail.service.PmsSkuStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
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
    @Autowired
    private PmsProductService pmsProductService;
    @Autowired
    private PmsSkuStockService pmsSkuStockService;

    @ApiOperation("购物车列表删除商品")
    @PostMapping(value = "/batchDel")
    public CommonResult<Boolean> batchDel(@RequestParam("ids") @NotEmpty List<Long> ids) {
        // 购物车列表删除商品
        return cartItemService.batchDelCart(ids);
    }

    @ApiOperation("添加商品到购物车")
    @PostMapping(value = "/add")
    public CommonResult<Boolean> addCarItem(@Validated @RequestBody OmsCartItemParam param) {
        // TODO 被修改 添加商品到购物车
        return cartItemService.addCarItem(param);
    }

    @ApiOperation("获取购物车信息")
    @PostMapping(value = "/getCarItem")
    public CommonResult<List<OmsCartItemDTO>> getCarItem() {
        // 获取购物车信息
        List<OmsCartItem> cartItemList = cartItemService.getCarItem();
        List<OmsCartItemDTO> dto = new ArrayList<>();
        for (int i = 0; i < cartItemList.size(); i++) {
            OmsCartItemDTO entity = new OmsCartItemDTO();
            BeanUtil.copyProperties(cartItemList.get(i), entity);
            //查询商品
            PmsProduct pmsProduct = pmsProductService.getById(cartItemList.get(i).getProductId());
            if (pmsProduct != null) {
                entity.setProductName(pmsProduct.getProductName());
                entity.setPic(pmsProduct.getPic());
                entity.setPublishStatus(pmsProduct.getPublishStatus());
            }
            //查询库存
            PmsSkuStock pmsSkuStock = pmsSkuStockService.getById(cartItemList.get(i).getProductSkuId());
            if (pmsSkuStock != null) {
                entity.setSpData(pmsSkuStock.getSpData());
            }
            dto.add(entity);
        }
        return CommonResult.success("success", dto);
    }

    @ApiOperation("获取商品库存信息")
    @PostMapping(value = "/getPmsSkuStockStatus")
    public CommonResult<String> getPmsSkuStockStatus(@Validated @RequestParam("skuId") String skuId, @Validated @RequestParam("count") int count) {
        // 获取购物车信息
        QueryWrapper<PmsSkuStock> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_stock_id", skuId).gt("stock", count);
        int counts = pmsSkuStockService.count(wrapper);
        if (counts == 0) {
            return CommonResult.failed("该商品库存不足");
        } else {
            return CommonResult.success("库存:" + counts);
        }
    }
}
