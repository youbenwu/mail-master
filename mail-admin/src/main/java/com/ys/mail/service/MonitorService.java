package com.ys.mail.service;

import java.util.Map;

/**
 * @Desc 系统监控接口
 * @Author CRH
 * @Create 2022-03-04 17:47
 */
public interface MonitorService {

    /**
     * 查询服务器硬件、运行环境等数据
     *
     * @param refresh 是否强制刷新
     * @return map
     */
    Map<String, Object> getServers(boolean refresh);
}
