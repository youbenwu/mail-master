package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SysProductLog;
import com.ys.mail.model.dto.SysProductLogDTO;

import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-07 11:31
 * @Email 18218292802@163.com
 */
public interface PcSysProductLogService extends IService<SysProductLog> {

    /**
     * 我商品浏览的足迹
     *
     * @param userId       用户id
     * @param productLogId 商品日志id
     * @return 返回值
     */
    List<SysProductLogDTO> selectAllProductLog(Long userId, Long productLogId);
}
