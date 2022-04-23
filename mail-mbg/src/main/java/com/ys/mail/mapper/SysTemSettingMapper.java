package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.SysTemSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统参数设置 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-12-02
 */
@Mapper
public interface SysTemSettingMapper extends BaseMapper<SysTemSetting> {

    SysTemSetting getOneByType(@Param("temType") Integer temType);

    int insertBatch(@Param("list") List<SysTemSetting> list);

    int updateBatch(@Param("list") List<SysTemSetting> list);
}
