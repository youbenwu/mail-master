package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ys.mail.entity.AmsApp;
import com.ys.mail.model.admin.query.AppQuery;
import com.ys.mail.model.admin.vo.AmsAppVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2022-05-13
 */
public interface AmsAppMapper extends BaseMapper<AmsApp> {

    /**
     * 后台分页查询
     *
     * @param page         分页对象
     * @param queryWrapper 筛选条件
     * @return 结果
     */
    IPage<AmsAppVO> getPage(IPage<AmsApp> page, @Param(Constants.WRAPPER) Wrapper<AmsApp> queryWrapper);

}
