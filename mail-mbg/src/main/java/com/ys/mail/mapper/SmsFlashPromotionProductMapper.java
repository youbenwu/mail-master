package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.model.admin.dto.PcFlashPdtDTO;
import com.ys.mail.model.admin.dto.PcFlashPromotionProductDTO;
import com.ys.mail.model.admin.dto.SessionOrPdtDTO;
import com.ys.mail.model.admin.dto.excel.SecKillCollectDTO;
import com.ys.mail.model.admin.dto.excel.UserSecKillDetailsDTO;
import com.ys.mail.model.dto.FlashPromotionProductDTO;
import com.ys.mail.model.dto.MyStoreDTO;
import com.ys.mail.model.dto.QuickBuyProductInfoDTO;
import com.ys.mail.model.po.QuickProductPO;
import com.ys.mail.model.query.QueryQuickBuy;
import com.ys.mail.model.query.QuickBuyProductQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品限时购与商品关系表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
@Mapper
public interface SmsFlashPromotionProductMapper extends BaseMapper<SmsFlashPromotionProduct> {
    /**
     * 根据id查询详情
     *
     * @param flashPromotionPdtId id
     * @return 返回值
     */
    PcFlashPromotionProductDTO selectInfoById(@Param("flashPromotionPdtId") Long flashPromotionPdtId);

    /**
     * 翻页查询
     *
     * @param page 翻页条件
     * @return 返回值
     */
    Page<PcFlashPdtDTO> listAll(@Param("page") Page<PcFlashPromotionProductDTO> page);

    /**
     * 返回首页的秒杀活动
     *
     * @return
     */
    List<FlashPromotionProductDTO> selectNewestSecond();

    /**
     * 根据秒杀时间段查找出关联的商品
     *
     * @param flashPromotionId id
     * @return 返回值
     */
    List<FlashPromotionProductDTO> getNewestSecondByFpId(@Param("flashPromotionId") Long flashPromotionId);

    /**
     * 返回店铺首页秒杀预告信息
     *
     * @return
     */
    List<FlashPromotionProductDTO> getFlashMessage(@Param("more") Boolean more, @Param("integralId") Long integralId);

    /**
     * 自营-发布产品页面信息
     *
     * @return
     */
    FlashPromotionProductDTO getFlashPublishMessage(@Param("integralId") Long integralId, @Param("nowDate") Date nowDate);

    /**
     * 返回店铺首页秒杀预告信息
     *
     * @return
     */
    List<FlashPromotionProductDTO> getUserFlashProduct(@Param("more") Boolean more, @Param("integralId") Long integralId, @Param("cpyType") Byte cpyType, @Param("userId") Long userId);

    /**
     * 返回店铺首页秒杀预告信息
     *
     * @return
     */
    List<FlashPromotionProductDTO> getFlashProductMesg(@Param("integralId") Long integralId, @Param("userId") Long userId);

    /**
     * 秒杀详情页面
     *
     * @param qo 查询对象
     * @return 返回值
     */
    QuickBuyProductInfoDTO quickBuyProductInfo(@Param("qo") QuickBuyProductQuery qo);

    /**
     * 查询
     *
     * @param qo 查询对象
     * @return 返回值
     */
    QuickProductPO selectQuickBuy(@Param("qo") QueryQuickBuy qo);

    /**
     * 减掉库存
     *
     * @param flashPromotionPdtId id
     * @param quantity            商品id
     * @return
     */
    boolean updateSale(@Param("flashPromotionPdtId") Long flashPromotionPdtId, @Param("quantity") Integer quantity);

    SmsFlashPromotionProduct selectGroupFlashPromotionCount(@Param("productId") String productId, @Param("price") Long price, @Param("flashPromotionId") Long flashPromotionId, @Param("cpyType") Byte cpyType);

    List<SmsFlashPromotionProduct> selectGroupFlashPromotionCountList(@Param("productId") String productId, @Param("price") Long price, @Param("flashPromotionId") Long flashPromotionId);

    void updateSale(@Param("map") Map<Long, Integer> map);

    void restoreInventory(@Param("map") Map<Long, Integer> map);

    List<SmsFlashPromotionProduct> selectUserIncomeSQL(@Param("flashPromotionId") Long flashPromotionId);

    void updateFlashPromotionId(@Param("flashPromotionId") Long flashPromotionId);

    Long selectToDayUserPrice(@Param("userids") List<Long> userids, @Param("promotionIds") List<Long> promotionIds);

    List<SmsFlashPromotionProduct> selectUserIncomeSQLQ(@Param("map") Map<Long, Integer> map);

    void replaceFlashPromotionId(@Param("flashPromotionId") Long flashPromotionId, @Param("replacedFlashPromotionId") Long replacedFlashPromotionId);

    /**
     * 最新场次和商品
     *
     * @return 返回值
     */
    SessionOrPdtDTO selectSessionOrPdt();

    SmsFlashPromotionProduct selectUserIncomeOneSQL(@Param("flashPromotionPdtId") Long flashPromotionPdtId);

    void updateFlashPromotionPdtId(@Param("flashPromotionPdtId") Long flashPromotionPdtId);

    /**
     * 返回首页的企业秒杀活动
     *
     * @return
     */
    List<FlashPromotionProductDTO> selectCpyNewestSecond();

    /**
     * 查询出用户未被秒杀的商品
     *
     * @param flashPromotionId 场次id
     * @return 返回值
     */
    List<Long> selectByPromotionId(@Param("flashPromotionId") Long flashPromotionId);

    /**
     * 修改用户未卖出的数量
     *
     * @param promotionProducts 集合
     * @return 返回数量
     */
    void updateByPromotionId(@Param("promotionProducts") List<SmsFlashPromotionProduct> promotionProducts);

    /**
     * 查询我的店铺
     *
     * @param pageSize 翻页长度
     * @param userId   用户id
     * @param cpyType  公司类型
     * @return 返回值
     */
    List<MyStoreDTO> selectMyStore(@Param("pageSize") Integer pageSize, @Param("userId") Long userId, @Param("cpyType") Byte cpyType);

    /**
     * 查询我的店铺更多
     *
     * @param cpyType             公司类型
     * @param flashPromotionPdtId 返回值
     * @param userId              用户id
     * @return 返回值
     */
    List<MyStoreDTO> selectAllProduct(@Param("cpyType") Byte cpyType, @Param("flashPromotionPdtId") Long flashPromotionPdtId, @Param("userId") Long userId);

    /**
     * 获取秒杀汇总数据
     *
     * @return 结果列表
     */
    List<SecKillCollectDTO> getSecKillCollect();

    /**
     * 获取用户秒杀详情数据
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<UserSecKillDetailsDTO> getUserFlashDetailsData(@Param("userId") Long userId);

}
