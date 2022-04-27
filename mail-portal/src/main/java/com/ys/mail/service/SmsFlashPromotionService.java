package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.MapQuery;
import com.ys.mail.model.bo.FlashPromotionProductBO;
import com.ys.mail.model.dto.SecondProductDTO;
import com.ys.mail.model.param.TimeShopParam;
import com.ys.mail.model.po.FlashPromotionProductPO;

import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-12 17:43
 */

public interface SmsFlashPromotionService extends IService<SmsFlashPromotion> {

    /**
     * 首页秒杀活动全部
     *
     * @param robBuyType 类型
     * @return 返回值
     */
    List<FlashPromotionProductPO> getAllNewestSecond(Byte robBuyType);

    /**
     * 生成秒杀订单
     *
     * @param param body
     * @return 返回值
     * @throws Exception 异常
     */
    CommonResult<Boolean> saveTimeShop(TimeShopParam param) throws Exception;

    /**
     * 线上发布
     *
     * @param orderId      订单id
     * @param productPrice 价格
     * @return 返回值
     */
    CommonResult<Boolean> savePostedOnline(String orderId, Long productPrice);

    /**
     * 查询首页中限时抢购的商品
     *
     * @param ite 判断高级用户普通用户
     * @return 返回值
     */
    SecondProductDTO getSecondProduct(Integer ite, Byte cpyType);

    /**
     * 首页秒杀活动全部翻页
     *
     * @param flashPromotionId    场次id
     * @param flashPromotionPdtId 翻页id
     * @param robBuyType          类型
     * @param mapQuery            经纬度查询对象
     * @return 返回值
     */
    List<FlashPromotionProductBO> getAllNewestSecondPage(String flashPromotionId, String flashPromotionPdtId, Byte robBuyType, MapQuery mapQuery);
}
