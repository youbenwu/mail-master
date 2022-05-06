package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.MapQuery;
import com.ys.mail.model.bo.GenerateOrderBO;
import com.ys.mail.model.dto.*;
import com.ys.mail.model.param.GenerateOrderParam;
import com.ys.mail.model.po.MyStorePO;
import com.ys.mail.model.query.QueryQuickBuy;
import com.ys.mail.model.query.QuickBuyProductQuery;
import com.ys.mail.model.vo.NearbyStoreProductVO;

import java.io.IOException;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-12 16:00
 */

public interface SmsFlashPromotionProductService extends IService<SmsFlashPromotionProduct> {

    /**
     * 首页限时秒杀
     *
     * @return 返回值
     */
    CommonResult<List<FlashPromotionProductDTO>> getNewestSecond();

    /**
     * 返回店铺首页秒杀预告信息
     *
     * @return 返回值
     */
    List<FlashPromotionProductDTO> getFlashMessage(Boolean more, Long integralId);

    /**
     * 自营-发布产品页面信息
     *
     * @return 返回值
     */
    FlashPromotionProductDTO getFlashPublishMessage(Long flashPromotionPdtId);

    /**
     * 自营-发布产品页面信息
     *
     * @return 返回值
     */
    List<FlashPromotionProductDTO> getUserFlashProduct(Boolean more, Long integralId, Byte cpyType);


    /**
     * 自营-上架用户秒杀产品
     *
     * @return 返回值
     */
    CommonResult<Boolean> addUserFlashProduct(Long flashPromotionPdtId);

    /**
     * 自营-秒杀产品详情
     *
     * @return 返回值
     */
    List<FlashPromotionProductDTO> getFlashProductMesg(Long integralId);

    /**
     * 秒杀详情页面
     *
     * @param qo 查询对象
     * @return 返回值
     */
    QuickBuyProductInfoDTO quickBuyProductInfo(QuickBuyProductQuery qo);

    /**
     * 商品立即秒杀页面
     *
     * @param qo 传参对象
     * @return 返回值
     */
    QuickProductDTO getQuickBuy(QueryQuickBuy qo);

    /**
     * 秒杀生成订单
     *
     * @param param 实体对象
     * @return 返回值
     */
    CommonResult<GenerateOrderBO> quickGenerateOrder(GenerateOrderParam param);

    /**
     * 订单付款后-确认收货
     *
     * @param orderSn 订单编号
     * @return
     */
    CommonResult<Object> confirmReceipt(String orderSn) throws IOException;

    SmsFlashPromotionDTO currentPlatformPromotionId(Byte cpyType);

    /**
     * 已卖出产品详情
     *
     * @param orderId 订单id
     * @return 返回值
     */
    HasSoldProductDTO getHasSoldProductInfo(Long orderId);

    /**
     * 手动触发回购
     *
     * @param flashPromotionPdtId id
     */
    CommonResult<Object> userIncome(Long flashPromotionPdtId);

    /**
     * 我的店铺
     *
     * @param pageSize 翻页长度
     * @param cpyType  公司类型订单
     * @return 返回值
     */
    MyStorePO getMyStore(Integer pageSize, Byte cpyType);

    /**
     * 我的店铺更多
     *
     * @param cpyType             公司类型
     * @param flashPromotionPdtId id
     * @return 返回值
     */
    List<MyStoreDTO> getAllProduct(Byte cpyType, String flashPromotionPdtId);

    /**
     * 获取附近店铺信息
     *
     * @param flashPromotionId 秒杀场次ID
     * @param productType      商品类型：如：0->公司，1->用户上架
     * @param radius           半径，默认5公里
     * @param mapQuery         经纬度
     * @param partnerId        合伙人ID，以该店铺为中心
     * @return 结果
     */
    NearbyStoreProductVO getNearbyStore(Long flashPromotionId, Integer productType, Double radius, MapQuery mapQuery, Long partnerId);
}
