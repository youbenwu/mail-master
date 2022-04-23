package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SmsHomeAdvertise;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.HomeAdvertiseParam;
import com.ys.mail.model.admin.query.HomeAdvertiseQuery;

/**
 * <p>
 * 首页轮播广告表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-03
 */
public interface SmsHomeAdvertiseService extends IService<SmsHomeAdvertise> {
    /**
     * 添加和编辑
     * @param param 实体对象
     * @return 返回值
     */
    CommonResult<Boolean> create(HomeAdvertiseParam param);

    /**
     * 查询参数列表
     * @param query 查询对象
     * @return 返回值
     */
    Page<SmsHomeAdvertise> list(HomeAdvertiseQuery query);

    /**
     * 删除轮播图
     * @param homeAdvId 删除
     * @return 返回值
     */
    boolean delete(Long homeAdvId);
}
