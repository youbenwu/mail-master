package com.ys.mail.controller;

import com.ys.mail.annotation.ApiBlock;
import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.enums.RegularEnum;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.MapQuery;
import com.ys.mail.model.bo.FlashPromotionProductBO;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.*;
import com.ys.mail.model.param.GenerateOrderParam;
import com.ys.mail.model.po.FlashPromotionProductPO;
import com.ys.mail.model.po.MyStorePO;
import com.ys.mail.model.query.QueryQuickBuy;
import com.ys.mail.model.query.QuickBuyProductQuery;
import com.ys.mail.model.vo.NearbyStoreProductVO;
import com.ys.mail.model.vo.ShoppingMsgVO;
import com.ys.mail.service.SmsFlashPromotionHistoryService;
import com.ys.mail.service.SmsFlashPromotionProductService;
import com.ys.mail.service.SmsFlashPromotionService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-12 16:00
 */
@Validated
@RestController
@RequestMapping("/flash/promotion/product")
@Api(tags = "限时抢购管理")
public class SmsFlashPromotionProductController {

    @Autowired
    private SmsFlashPromotionProductService flashPromotionProductService;
    @Autowired
    private SmsFlashPromotionService flashPromotionService;
    @Autowired
    private SmsFlashPromotionHistoryService historyService;

    @ApiOperation("首页秒杀活动全部-DT")
    @GetMapping(value = "/getAllNewestSecond")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "robBuyType", value = "0->公司，1->用户上架，默认查所有", dataType = "Boolean")
    })
    public CommonResult<List<FlashPromotionProductPO>> getAllNewestSecond(@RequestParam(required = false) @Range(min = 0, max = 1) Byte robBuyType,
                                                                          MapQuery mapQuery) {
        List<FlashPromotionProductPO> result = flashPromotionService.getAllNewestSecond(robBuyType, mapQuery);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "首页秒杀活动全部翻页-DT", notes = "当经纬度为空时则默认以北京天安门为中心点计算：[39.909652,116.404177]")
    @GetMapping(value = "/getAllNewestSecondPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionId", value = "场次id，必传", dataType = "Long", required = true),
            @ApiImplicitParam(name = "flashPromotionPdtId", value = "秒杀id，用于翻页，不传默认为0", dataType = "Long"),
            @ApiImplicitParam(name = "robBuyType", value = "0->公司，1->用户上架，不传默认查全部", dataType = "Boolean")
    })
    public CommonResult<List<FlashPromotionProductBO>> getAllNewestSecondPage(@RequestParam("flashPromotionId")
                                                                              @NotBlank @Pattern(regexp = "^\\d{19}$") String flashPromotionId,
                                                                              @RequestParam(value = "flashPromotionPdtId", defaultValue = "0")
                                                                              @NotBlank @BlankOrPattern(regexp = "^0|\\d{19}$") String flashPromotionPdtId,
                                                                              @RequestParam(required = false) @Range(min = 0, max = 1) Byte robBuyType,
                                                                              MapQuery mapQuery) {

        List<FlashPromotionProductBO> bos = flashPromotionService.getAllNewestSecondPage(flashPromotionId, flashPromotionPdtId, robBuyType, mapQuery);
        return CommonResult.success(bos);
    }

    @ApiOperation("自营-上架用户秒杀产品")
    @PostMapping(value = "/addUserFlashProduct")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionPdtId", value = "秒杀产品ID", dataType = "Long", required = true)
    })
    public CommonResult<Boolean> addUserFlashProduct(@RequestParam("flashPromotionPdtId") Long flashPromotionPdtId) {
        return flashPromotionProductService.addUserFlashProduct(flashPromotionPdtId);
    }

    @ApiOperation("自营-秒杀产品详情")
    @PostMapping(value = "/getFlashProductMesg")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionPdtId", value = "秒杀产品ID", dataType = "Long", required = true)
    })
    public CommonResult<List<FlashPromotionProductDTO>> getFlashProductMesg(@RequestParam("flashPromotionPdtId") Long flashPromotionPdtId) {
        List<FlashPromotionProductDTO> result = flashPromotionProductService.getFlashProductMesg(flashPromotionPdtId);
        return CommonResult.success(result);
    }

    @ApiOperation("秒杀详情页面-DT")
    @PostMapping(value = "/info")
    public CommonResult<QuickBuyProductInfoDTO> info(@Validated @RequestBody QuickBuyProductQuery qo) {
        // 秒杀对应的规格
        QuickBuyProductInfoDTO result = flashPromotionProductService.quickBuyProductInfo(qo);
        return BlankUtil.isEmpty(result) ? CommonResult.failed(BusinessErrorCode.GOODS_NOT_EXIST) : CommonResult.success(result);
    }

    @ApiOperation("商品立即秒杀页面-DT")
    @PostMapping(value = "/getQuickBuy")
    public CommonResult<QuickProductDTO> getQuickBuy(@Validated @RequestBody QueryQuickBuy qo) {
        // 生成秒杀页面,需要再确认一下价格,价格是发布价,一致就OK了,秒杀数量只有一件,需要带过来一个主键id
        QuickProductDTO result = flashPromotionProductService.getQuickBuy(qo);
        return BlankUtil.isEmpty(result) || BlankUtil.isEmpty(result.getPo()) ? CommonResult.failed(BusinessErrorCode.GOODS_NOT_EXIST) : CommonResult.success(result);
    }

    @ApiBlock
    @ApiOperation("秒杀生成订单-DT")
    @PostMapping(value = "/quickGenerateOrder")
    @LocalLockAnn(key = "quickGenerateOrder:arg[0]")
    public CommonResult<GenerateOrderBO> quickGenerateOrder(@Validated @RequestBody GenerateOrderParam param) {
        // 秒杀生成订单
        return flashPromotionProductService.quickGenerateOrder(param);
    }

    @LocalLockAnn(key = "confirmReceipt:arg[0]")
    @ApiOperation("用户点击-确认收货-进入商品置换")
    @PostMapping(value = "/confirmReceipt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单编号", dataType = "String", required = true),
    })
    public CommonResult<Object> confirmReceipt(@RequestParam("orderSn") @NotBlank String orderSn) throws IOException {
        return flashPromotionProductService.confirmReceipt(orderSn);
    }

    /**
     * 返回当前平台  限时购id
     */
    @ApiOperation("返回当前平台-最近(含已开始的)上线的限时购id-可能为Null")
    @PostMapping(value = "/currentPlatformPromotionId/{cpyType:^0|^1}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cpyType", value = "0->大尾狐,1->呼啦兔", dataType = "Byte", required = true)
    })
    public CommonResult<Object> currentPlatformPromotionId(@PathVariable Byte cpyType, @RequestParam("more") Boolean more) {
        SmsFlashPromotionDTO result = flashPromotionProductService.currentPlatformPromotionId(cpyType);
        return CommonResult.success(result);
    }

    @ApiOperation("已卖出产品详情")
    @PostMapping(value = "/getHasSold/{orderId:^\\d{19}$}/info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "String", required = true)
    })
    public CommonResult<HasSoldProductDTO> getHasSoldProductInfo(@PathVariable Long orderId) {
        // 取用图片
        HasSoldProductDTO result = flashPromotionProductService.getHasSoldProductInfo(orderId);
        return BlankUtil.isEmpty(result) ? CommonResult.failed(BusinessErrorCode.NOT_ORDER_LIST) : CommonResult.success(result);
    }


//    @ApiOperation("手动触发退款")
//    @PostMapping(value = "/userIncome/{flashPromotionPdtId}")
//    @LocalLockAnn(key = "userIncome:arg[0]")
//    public CommonResult<Object> userIncome(@PathVariable Long flashPromotionPdtId) {
//        return flashPromotionProductService.userIncome(flashPromotionPdtId);
//    }

    /**
     * 我的店铺中快购信息通知,我的产品出现慢查询,极度影响性能,建议join不能超过2层,如果出现超过2层,那就是建表的问题
     * 本身历史记录就是一个历史数据,何来的连多张表查询,建表的时候应考虑需要的字段,怎么查,数据库io是及其珍贵的性能,一条慢查询sql将耗尽主线程
     * 进来的时候封装一个对象和一个list集合,list集合进行翻页,
     */
    @ApiOperation("我的店铺-DT")
    @GetMapping(value = "/getMyStore")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "翻页长度,默认20条", dataType = "Integer"),
            @ApiImplicitParam(name = "cpyType", value = "公司类型:0->大尾狐,1->呼啦兔", dataType = "Byte", required = true)
    })
    public CommonResult<MyStorePO> getMyStore(@RequestParam(value = "pageSize", defaultValue = "20") @Range(min = 1, max = 30, message = "翻页长度取值{1-30}") Integer pageSize,
                                              @RequestParam(value = "cpyType") @Range(min = 0, max = 1, message = "取值范围{0,1}") Byte cpyType) {
        MyStorePO result = flashPromotionProductService.getMyStore(pageSize, cpyType);
        return CommonResult.success(result);
    }

    @ApiOperation("我的店铺更多-DT")
    @GetMapping(value = "/getAllProduct/{cpyType:^0|^1}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cpyType", value = "公司类型:0->大尾狐,1->呼啦兔", dataType = "Byte", required = true),
            @ApiImplicitParam(name = "flashPromotionPdtId", value = "翻页id，分页条数为20条，可选", dataType = "String")
    })
    public CommonResult<List<MyStoreDTO>> getAllProduct(@PathVariable Byte cpyType,
                                                        @RequestParam(value = "flashPromotionPdtId", required = false) @BlankOrPattern(regexp = "\\d{19}$") String flashPromotionPdtId) {
        // 翻页id传空,第二次,公司类型,最后一个id
        List<MyStoreDTO> result = flashPromotionProductService.getAllProduct(cpyType, flashPromotionPdtId);
        return CommonResult.success(result);
    }

    @ApiOperation("快购消息通知更多-DT")
    @GetMapping(value = "/getAllHistory")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "histroyId", value = "翻页id,首次可以不传,翻页传最后一个id", dataType = "String")
    })
    public CommonResult<List<ShoppingMsgVO>> getAllHistory(@RequestParam(value = "histroyId", required = false) @Pattern(regexp = "\\d{19}$") String histroyId) {
        List<ShoppingMsgVO> result = historyService.getAllHistory(histroyId);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "附近秒杀店铺定位", notes = "当经纬度为空时则默认以北京天安门为中心点计算：[39.909652,116.404177]")
    @GetMapping(value = "/getNearbyStore")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionId", value = "场次id，必传", dataType = "Long", required = true),
            @ApiImplicitParam(name = "productType", value = "产品类型：0->公司、供应商，1->用户上架", dataType = "int", required = true),
            @ApiImplicitParam(name = "radius", value = "以定位为中心点的半径范围，单位m，默认：500000，表示500公里", dataType = "double"),
            @ApiImplicitParam(name = "partnerId", value = "店铺ID，默认为最近的店铺ID", dataType = "Long")
    })
    public CommonResult<NearbyStoreProductVO> getNearbyStore(@RequestParam("flashPromotionId")
                                                             @NotNull @BlankOrPattern(regEnum = RegularEnum.ID) Long flashPromotionId,
                                                             @RequestParam(value = "productType") Integer productType,
                                                             @RequestParam(value = "radius", required = false) Double radius,
                                                             @RequestParam(value = "partnerId", required = false)
                                                             @BlankOrPattern(regEnum = RegularEnum.ID) Long partnerId,
                                                             MapQuery mapQuery) {
        NearbyStoreProductVO vo = flashPromotionProductService.getNearbyStore(flashPromotionId, productType, radius, mapQuery, partnerId);
        return CommonResult.success(vo);
    }

    /**
     * 手动触发退款
     */
    @ApiOperation("我的店铺退款")
    @PostMapping(value = "/userIncome/{id:^\\d{19}$}")
    @LocalLockAnn(key = "userIncome:arg[0]")
    public CommonResult<Boolean> userIncome(@PathVariable("id") Long flashPromotionPdtId){

        return flashPromotionProductService.refund(flashPromotionPdtId);
    }

}
