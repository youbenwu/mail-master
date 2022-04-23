package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.entity.PmsPartnerProduct;
import com.ys.mail.model.dto.PartnerProductDTO;
import com.ys.mail.model.po.PartnerProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合伙人产品表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2022-02-24
 */
@Mapper
public interface PmsPartnerProductMapper extends BaseMapper<PmsPartnerProduct> {

    /**
     * 查询
     *
     * @param more         参数
     * @param partnerPdtId id
     * @param totalPriceMax 参数
     * @return 返回值
     */
    List<PartnerProductDTO> selectList(@Param("more") Byte more, @Param("partnerPdtId") Long partnerPdtId,@Param("totalPriceMax") Long totalPriceMax);

    /**
     * 查询详情
     *
     * @param partnerPdtId 主键id
     * @return 返回值
     */
    PartnerProductPO selectByPrPdtId(@Param("partnerPdtId") Long partnerPdtId);

    /**
     * 查询立即购买页面
     *
     * @param partnerPdtId id
     * @return 返回值
     */
    PartnerProductDTO selectBuy(@Param("partnerPdtId") Long partnerPdtId);

    /**
     * 插入订单信息
     *
     * @param order     订单
     * @param orderItem 子订单
     * @return 返回值
     */
    boolean insertOrder(@Param("order") OmsOrder order, @Param("orderItem") OmsOrderItem orderItem);
}
