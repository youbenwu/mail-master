package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.SysDistrict;
import com.ys.mail.model.tree.SysDistrictTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 省市区数据字典表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-12-04
 */
@Mapper
public interface SysDistrictMapper extends BaseMapper<SysDistrict> {
    /**
     * 获取树列表
     * @return 返回值
     */
    List<SysDistrictTree> trees();
}
