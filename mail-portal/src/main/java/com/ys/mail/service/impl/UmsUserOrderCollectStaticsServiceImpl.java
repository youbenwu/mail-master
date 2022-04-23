package com.ys.mail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ys.mail.mapper.UmsUserOrderCollectStaticsMapper;
import com.ys.mail.model.vo.UmsUserOrderCollectStaticsVO;
import com.ys.mail.service.UmsUserOrderCollectStaticsService;
import com.ys.mail.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-07 19:26
 * @Email 18218292802@163.com
 */
@Service
public class UmsUserOrderCollectStaticsServiceImpl implements UmsUserOrderCollectStaticsService {

    @Autowired
    private UmsUserOrderCollectStaticsMapper mapper;

    @Override
    public UmsUserOrderCollectStaticsVO getOrderCollectStaticsInfo(Long userId, String cpyType) {
        if (BeanUtil.isEmpty(userId)) userId = UserUtil.getCurrentUser().getUserId();
        // 先查询收藏，足迹，和悦券的总数
        UmsUserOrderCollectStaticsVO staticsVO = mapper.getProductCollectStaticsInfo(userId);
        // 再查询订单状态信息
        UmsUserOrderCollectStaticsVO orderInfo = mapper.getOrderStatusStaticsInfo(userId, cpyType);
        // 组装数据到一个对象
        staticsVO.setOrderStatusNoPayCount(orderInfo.getOrderStatusNoPayCount());
        staticsVO.setOrderStatusNoDeliverCount(orderInfo.getOrderStatusNoDeliverCount());
        staticsVO.setOrderStatusNoChargedCount(orderInfo.getOrderStatusNoChargedCount());
        staticsVO.setOrderStatusNoAppraiseCount(orderInfo.getOrderStatusNoAppraiseCount());

        return staticsVO;
    }
}
