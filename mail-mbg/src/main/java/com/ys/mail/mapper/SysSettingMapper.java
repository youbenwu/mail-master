package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.SysSetting;
import com.ys.mail.model.admin.vo.SysSettingVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 全局设置表（可以新增，但只允许修改部分字段） Mapper 接口
 * </p>
 *
 * @author 007
 * @since 2022-02-14
 */
@Mapper
public interface SysSettingMapper extends BaseMapper<SysSetting> {

    Page<SysSettingVo> getPage(Page<SysSettingVo> page, @Param(Constants.WRAPPER) Wrapper<SysSettingVo> wrapper);

    List<String> getGroupName();

}
