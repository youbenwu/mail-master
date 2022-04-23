package com.ys.mail.mapper;

import com.ys.mail.model.admin.vo.statistics.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Desc 数据统计Mapper
 * @Author CRH
 * @Create 2022-02-21 15:24
 */
@Mapper
public interface DataStatisticsMapper {

    /**
     * 统计后台当日审核汇总数据
     *
     * @param date 日期，如：2022-03-02
     * @return 统计信息
     */
    PcReviewCollectDataVo getReviewDataByDate(String date);

    /**
     * 按订单类型分组统计数据
     *
     * @param date 日期，如：2022-03-02
     * @return 统计信息
     */
    List<PcOrderCollectDataVo> getOrderDataByDate(String date);

    /**
     * 获取用户汇总数据
     *
     * @return 统计信息
     */
    UserCollectDataVo getUserCollectData();

    /**
     * 按年月获取秒杀订单数据
     *
     * @param date 年月，如：2022-03
     * @return 统计信息
     */
    List<EveryMonthCollectDataVo> getSaleOrderDataByYearMonth(String date);

    /**
     * 按年月获取提现审核数据
     *
     * @param date 年月，如：2022-03
     * @return 统计信息
     */
    List<EveryMonthCollectDataVo> getExIncomeDataByYearMonth(String date);

    /**
     * 分组获取所有用户的邀请人数
     *
     * @return 统计信息
     */
    List<InviteLadderListVo> getAllUserInviteCount();

}
