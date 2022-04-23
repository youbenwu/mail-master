package com.ys.mail.service;

import com.ys.mail.model.vo.UmsUserOrderCollectStaticsVO;

/**
 * <p>
 * 用户邀请信息表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
public interface UmsUserOrderCollectStaticsService {

    UmsUserOrderCollectStaticsVO getOrderCollectStaticsInfo(Long userId, String cpyType);

}
