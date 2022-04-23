package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.constant.FigureConstant;
import com.ys.mail.entity.PmsPartnerRe;
import com.ys.mail.mapper.PmsPartnerReMapper;
import com.ys.mail.model.admin.dto.PcPartnerReDTO;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.vo.PartnerReIncomeVO;
import com.ys.mail.service.PmsPartnerReService;
import com.ys.mail.util.BlankUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 合伙人退还期数表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-03-01
 */
@Service
public class PmsPartnerReServiceImpl extends ServiceImpl<PmsPartnerReMapper, PmsPartnerRe> implements PmsPartnerReService {

    @Autowired
    private PmsPartnerReMapper partnerReMapper;

    @Override
    public List<PartnerReIncomeVO> getByMonthFirstDates(String format) {
        return partnerReMapper.selectByMonthFirstDates(format);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateBatch(String format) {
        return partnerReMapper.updateBatch(format);
    }

    @Override
    public Page<PcPartnerReDTO> list(Query qo) {
        Page<PcPartnerReDTO> page = new Page<>(BlankUtil.isEmpty(qo.getPageNum()) ? NumberUtils.INTEGER_ONE : qo.getPageNum(), BlankUtil.isEmpty(qo.getPageSize()) ? FigureConstant.INT_ONE_ZERO : qo.getPageSize());
        qo.setBeginTime(BlankUtil.isEmpty(qo.getBeginTime()) ? FigureConstant.STRING_EMPTY : qo.getBeginTime().substring(NumberUtils.INTEGER_ZERO, FigureConstant.INT_ONE_ZERO));
        return partnerReMapper.selectList(page, qo);
    }
}
