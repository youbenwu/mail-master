package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcFlashPromotionParam;

import java.text.ParseException;

/**
 * <p>
 * 限时购表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
public interface SmsFlashPromotionService extends IService<SmsFlashPromotion> {
    /**
     * 删除
     * @param flashPromotionId id
     * @return 返回true和false
     */
    boolean delFlashPromotion(Long flashPromotionId) throws ParseException;

    /**
     * 新增或修改限时购
     * @param param 实体对象
     * @return 返回值
     */
    boolean createFlashPromotion(PcFlashPromotionParam param) throws ParseException;

    /**
     * 发布秒杀商品
     * @param param 实体对象
     * @return 返回值
     */
    boolean publicFlashProduct(SmsFlashPromotionProduct smsFlashPromotionProduct);

    /**
     * 修改是否展示于首页
     * @param flashPromotionId id
     * @param homeStatus 状态
     * @return 返回值
     */
    CommonResult<Boolean> updateHome(Long flashPromotionId,Boolean homeStatus);

    /**
     * 修改上架状态
     * @param flashPromotionId id
     * @param publishStatus 状态
     * @return 返回值
     */
    boolean updatePublish(Long flashPromotionId, Boolean publishStatus) throws ParseException;
}
