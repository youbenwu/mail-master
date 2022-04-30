package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.model.admin.query.MapQuery;
import com.ys.mail.model.bo.FlashPromotionProductBO;
import com.ys.mail.model.dto.HasSoldProductDTO;
import com.ys.mail.model.dto.SecondProductDTO;
import com.ys.mail.model.dto.SmsFlashPromotionDTO;
import com.ys.mail.model.dto.TemporaryWorkerMasters;
import com.ys.mail.model.po.FlashPromotionProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 限时购表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
@Mapper
public interface SmsFlashPromotionMapper extends BaseMapper<SmsFlashPromotion> {
    /**
     * 首页秒杀活动全部
     *
     * @param robBuyType 类型
     * @param mapQuery   经纬度
     * @return 返回值
     */
    List<FlashPromotionProductPO> selectAllNewestSecond(@Param("robBuyType") Byte robBuyType,
                                                        @Param("mapQuery") MapQuery mapQuery);

    /**
     * 查询首页中限时抢购的商品
     *
     * @param cpyType 公司类型
     * @return 返回值
     */
    SecondProductDTO selectSecondProduct(@Param("cpyType") Byte cpyType);

    /**
     * 首页秒杀活动全部翻页
     *
     * @param flashPromotionId    场次id
     * @param flashPromotionPdtId 翻页id
     * @param robBuyType          类型
     * @param mapQuery            经纬度
     * @param isOpenPage          是否开启分页
     * @return 返回值
     */
    List<FlashPromotionProductBO> selectAllNewestSecondPage(@Param("flashPromotionId") Long flashPromotionId,
                                                            @Param("flashPromotionPdtId") Long flashPromotionPdtId,
                                                            @Param("robBuyType") Byte robBuyType,
                                                            @Param("mapQuery") MapQuery mapQuery,
                                                            @Param("isOpenPage") boolean isOpenPage);

    String selectByEndTime(@Param("flashPromotionId") Long flashPromotionId);

    SecondProductDTO selectCpyTypeOne(@Param("cpyType") Byte cpyType);

    SmsFlashPromotion selectTime(@Param("timeStr") String timeStr);

    SmsFlashPromotionDTO currentPlatformPromotionId(@Param("cpyType") Byte cpyType);

    List<Long> selectToDayPromotionIds();

    List<TemporaryWorkerMasters> findFlashPromotionId();

    TemporaryWorkerMasters findFlashPromotionIds(@Param("date") String yyyyMMdd);

    SmsFlashPromotion findNewestFlashPromotion(@Param("flashPromotionId") Long flashPromotionId, @Param("cpyType") Byte cpyType);

    /**
     * 修改
     *
     * @return 返回值
     */
    boolean updateHome();

    /**
     * 已卖出产品详情
     *
     * @param orderId 订单id
     * @return 返回值
     */
    HasSoldProductDTO selectHasSoldProductInfo(@Param("orderId") Long orderId);
}
