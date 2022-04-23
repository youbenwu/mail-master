package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SysProductLog;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.SysProductLogDTO;

import java.util.List;

/**
 * <p>
 * 商品足迹表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-19
 */
public interface SysProductLogService extends IService<SysProductLog> {
    /**
     * 我的足迹
     * @param productLogId 商品id
     * @return 返回值
     */
    List<SysProductLogDTO> getAllProductLog(Long productLogId);

    /**
     * 足迹批量删除
     * @param ids 集合id
     * @return 返回值
     */
    CommonResult<Boolean> batchDelProductLog(List<Long> ids);
}
