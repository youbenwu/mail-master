package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsPartnerRe;
import com.ys.mail.model.admin.dto.PcPartnerReDTO;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.vo.PartnerReIncomeVO;

import java.util.List;

/**
 * <p>
 * 合伙人退还期数表 服务类
 * </p>
 *
 * @author 070
 * @since 2022-03-01
 */
public interface PmsPartnerReService extends IService<PmsPartnerRe> {

    /**
     * 查询出最新的收益和返还
     *
     * @param format String
     * @return 返回值
     */
    List<PartnerReIncomeVO> getByMonthFirstDates(String format);

    /**
     * 批量修改每月的返还
     *
     * @param format 时间
     * @return 返回数量
     */
    int updateBatch(String format);

    /**
     * 条件查询
     *
     * @param qo 参数
     * @return 返回值
     */
    Page<PcPartnerReDTO> list(Query qo);
}
