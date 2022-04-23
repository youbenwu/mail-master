package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.model.admin.dto.excel.IncomeCollectDTO;
import com.ys.mail.model.admin.dto.excel.UserBalanceDTO;
import com.ys.mail.model.admin.vo.UmsIncomeVO;
import com.ys.mail.model.vo.UmsIncomeSumVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 收益表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-25
 */
@Mapper
public interface UmsIncomeMapper extends BaseMapper<UmsIncome> {
    /**
     * 查询用户最新的收益流水
     *
     * @param userId 用户id
     * @return 返回值
     */
    UmsIncome selectNewestByUserId(@Param("userId") Long userId);

    /**
     * 查询用户个人的汇总收益
     *
     * @param userId 用户ID
     * @return 汇总结果
     */
    UmsIncomeSumVO selectUmsIncomeSumById(@Param("userId") Long userId);

    /**
     * 分组查询指定用户ID列表的最新收益记录
     *
     * @param userIds 用户ID列表
     * @return 收益记录集合
     */
    List<UmsIncome> selectLatestByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 后台分页查看流水记录
     *
     * @param page         分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    Page<UmsIncomeVO> getPage(Page<UmsIncomeVO> page, @Param(Constants.WRAPPER) Wrapper<UmsIncomeVO> queryWrapper);

    /**
     * 批量插入收益数据
     *
     * @param list 收益对象列表
     * @return 操作结果
     */
    int insertBatch(@Param("list") List<UmsIncome> list);

    /**
     * 获取收益汇总数据
     *
     * @return 列表
     */
    List<IncomeCollectDTO> getIncomeCollect();

    /**
     * 获取用户最新余额
     *
     * @return 列表
     */
    List<UserBalanceDTO> getUserBalance();

}
