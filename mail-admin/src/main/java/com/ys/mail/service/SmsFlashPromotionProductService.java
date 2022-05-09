package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.model.admin.dto.PcFlashPdtDTO;
import com.ys.mail.model.admin.dto.PcFlashPromotionProductDTO;
import com.ys.mail.model.admin.dto.SessionOrPdtDTO;
import com.ys.mail.model.admin.param.PcFlashPromotionProductParam;
import com.ys.mail.model.admin.query.Query;

/**
 * <p>
 * 商品限时购与商品关系表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
public interface SmsFlashPromotionProductService extends IService<SmsFlashPromotionProduct> {
    /**
     * 新增或修改限时购商品
     *
     * @param param
     * @return
     */
    boolean create(PcFlashPromotionProductParam param);

    /**
     * 限时购商品详情
     *
     * @param flashPromotionPdtId 主键
     * @return 返回对象
     */
    PcFlashPromotionProductDTO getInfoById(Long flashPromotionPdtId);

    /**
     * 查询翻页列表
     *
     * @param query 查询条件
     * @return 返回值
     */
    Page<PcFlashPdtDTO> list(Query query);

    /**
     * 限时购商品删除
     *
     * @param flashPromotionPdtId id
     * @return 返回值
     */
    boolean delete(Long flashPromotionPdtId);

    /**
     * 置换场次id
     *
     * @param flashPromotionId         新id
     * @param replacedFlashPromotionId 被更换的id
     */
    void replaceFlashPromotionId(Long flashPromotionId, Long replacedFlashPromotionId);

    /**
     * 最新场次和商品
     *
     * @return 返回值
     */
    SessionOrPdtDTO getSessionOrPdt();

    /**
     * 清除首页秒杀缓存
     *
     * @param b 是否清除
     */
    void delHomeSecondProduct(boolean b);
}
