package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.SmsFlashPromotionHistory;
import com.ys.mail.mapper.SmsFlashPromotionHistoryMapper;
import com.ys.mail.model.vo.ShoppingMsgVO;
import com.ys.mail.service.SmsFlashPromotionHistoryService;
import com.ys.mail.util.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 秒杀历史记录表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
@Service
public class SmsFlashPromotionHistoryServiceImpl extends ServiceImpl<SmsFlashPromotionHistoryMapper, SmsFlashPromotionHistory> implements SmsFlashPromotionHistoryService {

    @Autowired
    private SmsFlashPromotionHistoryMapper historyMapper;

    @Override
    public ShoppingMsgVO getShopMsg() {
        return historyMapper.selectShopMsg();
    }

    @Override
    public List<ShoppingMsgVO> getAllHistory(String histroyId) {
        Long id = BlankUtil.isEmpty(histroyId) ? null : Long.valueOf(histroyId);
        return historyMapper.selectAllHistory(id);
    }
}
