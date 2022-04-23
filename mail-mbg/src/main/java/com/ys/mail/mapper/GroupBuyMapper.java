package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.GroupBuy;
import com.ys.mail.model.dto.GroupBuyDTO;
import com.ys.mail.model.dto.GroupBuyInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 团购 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-13
 */
@Mapper
public interface GroupBuyMapper extends BaseMapper<GroupBuy> {

    /**
     * 查询最新的
     * @return 返回值
     */
    List<GroupBuyDTO> selectNewestGroupBuy();

    /**
     * 查询全部的限时抢购商品
     * @return 返回值
     */
    List<GroupBuyDTO> selectAllGroupBuy();

    /**
     * 查询出拼团详情
     * @param productId 商品id
     * @return 返回值
     */
    GroupBuyInfoDTO selectByProductIdInfo(@Param("productId") Long productId);
}
