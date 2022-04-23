package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PcReview;
import com.ys.mail.model.CommonResult;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
public interface ReviewService extends IService<PcReview> {

    /**
     * 查询个人审核记录
     *
     * @param reviewId 审核ID，用于分页
     * @param pageSize 分页大小
     * @return 列表
     */
    List<PcReview> selectList(Long reviewId, Long pageSize);

    /**
     * 获取当天最新的审核记录
     *
     * @param userId 用户ID
     * @param today  日期字符串，如 2022-03-18
     * @return 审核记录
     */
    PcReview getNewestRecord(Long userId, String today);

    /**
     * 取消审核提现
     *
     * @param reviewId 审核ID
     * @return 审核结果
     */
    CommonResult<Boolean> cancel(Long reviewId);
}
