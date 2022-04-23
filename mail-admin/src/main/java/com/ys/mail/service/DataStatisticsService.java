package com.ys.mail.service;

import com.ys.mail.model.admin.vo.statistics.PcReviewCollectDataVo;

import java.util.Map;

/**
 * @Desc 数据统计服务接口
 * @Author CRH
 * @Create 2022-02-21 15:14
 */
public interface DataStatisticsService {

    /**
     * 统计后台当日审核汇总数据
     *
     * @param date 日期，如：2022-03-02
     * @return 统计信息
     */
    PcReviewCollectDataVo getReviewDataByDate(String date);

    /**
     * 按订单类型分组统计数据，查询所有，不按时间查询
     *
     * @param refresh 是否强制刷新缓存
     * @return 统计信息
     */
    Map<String, Object> getAllOrderData(boolean refresh);

    /**
     * 根据日期获取当日的订单统计数据
     *
     * @param date    日期，如：2022-03-02
     * @param refresh 是否强制刷新缓存
     * @return 统计信息
     */
    Map<String, Object> getOrderDataByDate(String date, boolean refresh);

    /**
     * 按年月获取秒杀订单数据
     *
     * @param date    年月，如：2022-03
     * @param refresh 是否强制刷新缓存
     * @return 统计信息
     */
    Map<String, Object> getSaleOrderDataByYearMonth(String date, boolean refresh);

    /**
     * 按年月获取提现审核数据
     *
     * @param date    date 年月，如：2022-03
     * @param refresh 是否强制刷新缓存
     * @return 统计信息
     */
    Map<String, Object> getExIncomeDataByYearMonth(String date, boolean refresh);

    /**
     * 分页获取用户邀请天梯榜数据
     *
     * @param pageNum  当前页码
     * @param pageSize 分页大小
     * @return 统计信息
     */
    Map<String, Object> getUserInviteLadderList(int pageNum, int pageSize);
}
