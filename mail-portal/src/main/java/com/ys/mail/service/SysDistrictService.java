package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SysDistrict;
import com.ys.mail.model.tree.SysDistrictTree;

import java.util.List;

/**
 * <p>
 * 省市区数据字典表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-04
 */
public interface SysDistrictService extends IService<SysDistrict> {
    /**
     * 获取字典管理
     * @return 返回树
     */
    List<SysDistrictTree> trees();
}
