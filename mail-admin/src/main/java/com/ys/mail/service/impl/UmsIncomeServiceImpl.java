package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.mapper.UmsIncomeMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.UmsIncomeQuery;
import com.ys.mail.model.admin.vo.UmsIncomeVO;
import com.ys.mail.service.UmsIncomeService;
import com.ys.mail.wrapper.SqlQueryWrapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-30 20:47
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UmsIncomeServiceImpl extends ServiceImpl<UmsIncomeMapper, UmsIncome> implements UmsIncomeService {

    @Autowired
    private UmsIncomeMapper umsIncomeMapper;

    @Override
    public CommonResult<Page<UmsIncomeVO>> getPage(UmsIncomeQuery query) {
        Page<UmsIncomeVO> page = new Page<>(query.getPageNum(), query.getPageSize());

        SqlQueryWrapper<UmsIncomeVO> wrapper = new SqlQueryWrapper<>();
        wrapper.eq("uu.is_user_status", NumberUtils.INTEGER_ONE)
                .eq("uu.deleted", NumberUtils.INTEGER_ZERO)
                .eq("ui.deleted", NumberUtils.INTEGER_ZERO)
                .like("ui.income_type", query.getKeyword())
                .like("ui.user_id", query.getUserId())
                .like("ui.income_no", query.getIncomeNo())
                .like("uu.phone", query.getPhone())
                .compareDate("ui.create_time", query.getBeginTime(), query.getEndTime())
                .orderBy(true, Boolean.parseBoolean(query.getAsc()), "ui.income_id");

        return CommonResult.success(umsIncomeMapper.getPage(page, wrapper));
    }

    @Override
    public int insertBatch(List<UmsIncome> list) {
        return umsIncomeMapper.insertBatch(list);
    }

    @Override
    public List<UmsIncome> selectLatestByUserIds(List<Long> userIds) {
        return umsIncomeMapper.selectLatestByUserIds(userIds);
    }
}
