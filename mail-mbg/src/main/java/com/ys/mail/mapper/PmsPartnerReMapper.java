package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsPartnerRe;
import com.ys.mail.model.admin.dto.PcPartnerReDTO;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.vo.PartnerReIncomeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合伙人退还期数表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2022-03-01
 */
@Mapper
public interface PmsPartnerReMapper extends BaseMapper<PmsPartnerRe> {

    /**
     * 查询出最新的收益和返还
     *
     * @param format String
     * @return 返回值
     */
    List<PartnerReIncomeVO> selectByMonthFirstDates(@Param("format") String format);

    /**
     * 批量修改每月的数量
     *
     * @param format 时间
     * @return 返回值
     */
    int updateBatch(@Param("format") String format);

    /**
     * 查询参数
     *
     * @param qo   参数
     * @param page 翻页
     * @return 返回值
     */
    Page<PcPartnerReDTO> selectList(@Param("page") Page<PcPartnerReDTO> page, @Param("qo") Query qo);
}
