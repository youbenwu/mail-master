package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.GroupBuy;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.GroupBuyDTO;
import com.ys.mail.model.dto.GroupBuyInfoDTO;

import java.util.List;

/**
 * <p>
 * 团购 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-13
 */
public interface GroupBuyService extends IService<GroupBuy> {

    /**
     * 查询最新的几条拼单
     * @return 返回值
     */
    List<GroupBuyDTO> getNewestGroupBuy();

    /**
     * 首页全部拼团
     * @param pageNum
     * @return 返回值
     */
    List<GroupBuyDTO> getAllGroupBuy(Long pageNum);

    /**
     * 拼团详情页
     * @param productId 商品id
     * @return 返回对象
     */
    GroupBuyInfoDTO groupBuyInfo(Long productId);
}
