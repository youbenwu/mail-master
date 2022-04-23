package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.UmsIncomeQuery;
import com.ys.mail.model.admin.vo.UmsIncomeVO;

import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-30 20:46
 */
public interface UmsIncomeService extends IService<UmsIncome> {

    CommonResult<Page<UmsIncomeVO>> getPage(UmsIncomeQuery query);

    int insertBatch(List<UmsIncome> list);

    List<UmsIncome> selectLatestByUserIds(List<Long> userIds);
}
