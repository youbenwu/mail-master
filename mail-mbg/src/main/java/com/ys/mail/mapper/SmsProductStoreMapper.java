package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ys.mail.entity.SmsProductStore;
import com.ys.mail.model.admin.vo.SmsProductStoreVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户_商品_店铺表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2022-04-25
 */
public interface SmsProductStoreMapper extends BaseMapper<SmsProductStore> {

    /**
     * 后台分页查询
     *
     * @param page         分页对象
     * @param queryWrapper 筛选条件
     * @return 结果
     */
    IPage<SmsProductStoreVO> getPage(IPage<SmsProductStoreVO> page, @Param(Constants.WRAPPER) Wrapper<SmsProductStoreVO> queryWrapper);
}
