package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ys.mail.entity.PcReview;
import com.ys.mail.model.admin.dto.excel.ReviewCollectDTO;
import com.ys.mail.model.admin.vo.PcReviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
@Mapper
public interface PcReviewMapper extends BaseMapper<PcReview> {

    /**
     * 后台分页查询
     *
     * @param page         分页对象
     * @param queryWrapper 筛选条件
     * @return 结果
     */
    IPage<PcReviewVO> getPage(IPage<PcReviewVO> page, @Param(Constants.WRAPPER) Wrapper<PcReviewVO> queryWrapper);

    /**
     * 根据条件获取分页记录，不同的分页方式
     *
     * @param userId   用户ID
     * @param reviewId 审核ID，用于分页
     * @param pageSize 分页大小
     * @return 结果
     */
    List<PcReview> getList(@Param("userId") Long userId, @Param("reviewId") Long reviewId, @Param("pageSize") Long pageSize);

    /**
     * 获取指定日期的列表
     *
     * @param time 日期
     * @return 结果
     */
    List<PcReview> getTodayList(@Param("time") String time);

    /**
     * 获取当天待审核金额
     *
     * @return 结果
     */
    List<ReviewCollectDTO> getAwaitReview();

}
