package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.query.IncomeDimensionQuery;
import com.ys.mail.model.vo.UmsIncomeDimensionVO;
import com.ys.mail.model.vo.UmsIncomeSumVO;

import java.util.List;

/**
 * <p>
 * 收益表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-25
 */
public interface UmsIncomeService extends IService<UmsIncome> {

    /**
     * 根据参数获取用户收益
     *
     * @param userId 用户ID
     * @param type   收益类型
     * @return 收益对象
     */
    UmsIncome getUmsIncomeByIdType(Long userId, Integer type);

    /**
     * 查询个人最新的收益汇总记录
     *
     * @param userId 用户ID
     * @return VO
     */
    UmsIncomeSumVO getUmsIncomeSumById(Long userId);

    /**
     * 根据时间查询当前用户的历史收益记录，支持分页
     *
     * @param query 查询条件
     * @return List
     */
    CommonResult<List<UmsIncomeDimensionVO>> getUmsIncomeByDimension(IncomeDimensionQuery query);

    /**
     * 查询用户的最新一条收益
     *
     * @param userId 用户id
     * @return 返回值
     */
    UmsIncome selectNewestByUserId(Long userId);

    /**
     * 获取个人指定日期的流水次数
     *
     * @param userId     用户ID
     * @param incomeType 收益类型，如提现类型
     * @param today      日期
     * @return 数量
     */
    int getTodayCount(Long userId, Integer incomeType, String today);
}
