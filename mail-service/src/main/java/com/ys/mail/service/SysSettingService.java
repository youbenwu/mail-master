package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SysSetting;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.SysSettingParam;
import com.ys.mail.model.admin.query.SysSettingQuery;
import com.ys.mail.model.admin.vo.SysSettingVo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 全局设置表（可以新增，但只允许修改部分字段） 服务类
 * - 有操作设置表的服务统一在这里写，别额外调用，如果一下服务无法满足需求，则在该基础上扩展即可
 * - 另外所有类型必须通过枚举引用，避免直接使用数字
 * </p>
 *
 * @author 007
 * @since 2022-02-14
 */
public interface SysSettingService extends IService<SysSetting> {

    // 多条件分页列表
    CommonResult<Page<SysSettingVo>> getPage(SysSettingQuery query);

    // 单条查询
    CommonResult<SysSetting> getOne(Long sysSettingId);

    // 添加或修改
    CommonResult<Boolean> addOrUpdate(SysSettingParam param, Long pcUserId);

    // 删除单条
    CommonResult<Boolean> deleteOne(Long sysSettingId, Long pcUserId);

    // 读取数据库，强制刷新缓存，一般在更新操作才调用，平常调用 getCacheAll() 方法即可
    List<SysSetting> loadAll();

    // 从缓存中获取所有设置，当需要过滤时调用该方法
    List<SysSetting> getCacheAll();

    /**
     * 根据类型列表返回设置列表（多条）
     *
     * @param typeList 类型列表
     * @return 返回一批设置
     */
    List<SysSetting> getMatchList(List<SettingTypeEnum> typeList);

    /**
     * 根据设置类型查找单条设置，优先从缓存中读取
     * - 主要方便业务中读取
     *
     * @param settingType 设置类型，枚举
     * @return 返回整条设置记录（单条）
     */
    SysSetting getOneByType(SettingTypeEnum settingType);

    /**
     * 根据设置类型获取值，优先从缓存中读取
     * - 当值为空时，返回默认值
     * - 当被禁用时，返回null
     *
     * @param settingType 设置类型
     * @return 值
     */
    <V> V getSettingValue(SettingTypeEnum settingType);

    // 根据设置获取值
    <V> V getSettingValue(SysSetting sysSetting);

    // 直接获取默认值
    <V> V getSettingDefaultValue(SysSetting sysSetting);

    // 查询所有分组
    List<String> getGroupName();

    // 判断设置类型是否已存在
    Boolean isExist(SettingTypeEnum settingType);

    /**
     * 根据类型获取组名
     * - eg：sysSettingService.getGroupNameByType(EnumSettingType.two);
     *
     * @param settingType 设置类型，枚举
     * @return 组名
     */
    String getGroupNameByType(SettingTypeEnum settingType);

    // 根据组名获取该组设置列表
    List<SysSetting> getSettingByGroupName(String settingGroupName);

    // 分组获取所有设置类型
    Set<Integer> getAllSettingType();

    // 获取当前最大类型，不存在则返回 -1
    Integer getMaxSettingType();

    // 获取缓存中的key
    String getRedisKey();

}
