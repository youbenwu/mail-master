package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.SmsFlashPromotionHistory;
import com.ys.mail.model.vo.ShoppingMsgVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 秒杀历史记录表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
@Mapper
public interface SmsFlashPromotionHistoryMapper extends BaseMapper<SmsFlashPromotionHistory> {

    /**
     * 查询消息一条
     *
     * @return 返回值
     */
    ShoppingMsgVO selectShopMsg();

    /**
     * 查询所有的快购消息通知
     *
     * @param histroyId id
     * @return 返回值
     */
    List<ShoppingMsgVO> selectAllHistory(@Param("histroyId") Long histroyId);
}
