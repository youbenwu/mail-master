package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SmsFlashPromotionHistory;
import com.ys.mail.model.vo.ShoppingMsgVO;

import java.util.List;

/**
 * <p>
 * 秒杀历史记录表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
public interface SmsFlashPromotionHistoryService extends IService<SmsFlashPromotionHistory> {

    /**
     * 查询快购消息
     *
     * @return 返回值
     */
    ShoppingMsgVO getShopMsg();

    /**
     * 快购消息通知更多
     *
     * @param histroyId 主键id
     * @return 返回值
     */
    List<ShoppingMsgVO> getAllHistory(String histroyId);
}
