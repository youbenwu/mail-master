package com.ys.mail.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.SysSetting;
import com.ys.mail.enums.FormTypeEnum;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.enums.SettingValueTypeEnum;
import com.ys.mail.mapper.SysSettingMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.SysSettingParam;
import com.ys.mail.model.admin.query.SysSettingQuery;
import com.ys.mail.model.admin.vo.SysSettingVo;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.util.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 全局设置表（可以新增，但只允许修改部分字段） 服务实现类
 * </p>
 *
 * @author 007
 * @since 2022-02-14
 */
@Service
public class SysSettingServiceImpl extends ServiceImpl<SysSettingMapper, SysSetting> implements SysSettingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysSettingServiceImpl.class);

    @Autowired
    private SysSettingMapper sysSettingMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;

    @Override
    public CommonResult<Page<SysSettingVo>> getPage(SysSettingQuery query) {
        Page<SysSettingVo> page = new Page<>(query.getPageNum(), query.getPageSize());
        String beginTime = query.getBeginTime();
        String endTime = query.getEndTime();
        QueryWrapper<SysSettingVo> wrapper = new QueryWrapper<>();
        if (BlankUtil.isNotEmpty(query.getSettingGroupName()))
            wrapper.eq("ss.setting_group_name", query.getSettingGroupName());
        if (BlankUtil.isNotEmpty(query.getSettingType())) wrapper.eq("ss.setting_type", query.getSettingType());
        if (BlankUtil.isNotEmpty(query.getSettingKey())) wrapper.like("ss.setting_key", query.getSettingKey());
        if (BlankUtil.isNotEmpty(query.getEnable())) wrapper.eq("ss.is_enable", query.getEnable());
        if (BlankUtil.isNotEmpty(query.getAllowDisable())) wrapper.eq("ss.is_allow_disable", query.getAllowDisable());
        if (BlankUtil.isNotEmpty(query.getAllowDelete())) wrapper.eq("ss.is_allow_delete", query.getAllowDelete());
        if (!BlankUtil.isEmpty(beginTime) && !BlankUtil.isEmpty(endTime)) {
            if (beginTime.compareTo(endTime) > 0) return CommonResult.failed("开始时间不能大于结束时间");
            wrapper.between("date_format( ss.create_time,'%Y-%m-%d %T')", beginTime, endTime);
        }
        // 按设置类型升序
        wrapper.orderByAsc("ss.setting_type");
        Page<SysSettingVo> sysSettingPage = sysSettingMapper.getPage(page, wrapper);
        // 额外做一些数据处理，类型转换
        List<SysSettingVo> records = sysSettingPage.getRecords();
        if (BlankUtil.isNotEmpty(records)) {
            records.forEach(s -> {
                s.setValue(this.getSettingValue(s));
                s.setDefaultValue(this.getSettingDefaultValue(s));
            });
            sysSettingPage.setRecords(records);
        }
        return CommonResult.success(sysSettingPage);
    }

    @Override
    public CommonResult<SysSetting> getOne(Long sysSettingId) {
        SysSetting sysSetting = this.getById(sysSettingId);
        return BlankUtil.isNotEmpty(sysSetting) ? CommonResult.success(sysSetting) : CommonResult.failed("查询失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> addOrUpdate(SysSettingParam param, Long pcUserId) {
        SysSetting sysSetting = new SysSetting();
        BeanUtils.copyProperties(param, sysSetting);
        String sysSettingId = param.getSysSettingId();
        String settingType = param.getSettingType();
        SysSetting typeSetting = null;

        // 枚举检查
        if (!"-1".equals(settingType)) {
            SettingTypeEnum enumByNumber = SettingTypeEnum.getByType(settingType);
            if (BlankUtil.isEmpty(enumByNumber)) return CommonResult.failed("操作失败，该类型暂不能添加！", Boolean.FALSE);
            typeSetting = this.getOneByType(enumByNumber);
        }

        // 检查{值类型}是否正确
        boolean contains = EnumUtil.contains(SettingValueTypeEnum.class, param.getSettingValueType());
        if (!contains) return CommonResult.failed("操作失败，值类型不存在！", Boolean.FALSE);
        // 检查{表单类型}是否正确
        contains = EnumUtil.contains(FormTypeEnum.class, param.getSettingFormType());
        if (!contains) return CommonResult.failed("操作失败，表单类型不存在！", Boolean.FALSE);
        // 值与类型匹配检查：不通过则不能进行添加修改
        if (!ConvertTypeUtil.checkType(param.getSettingValue(), param.getSettingValueType()))
            return CommonResult.failed("操作失败，值与值类型不匹配！", Boolean.FALSE);
        if (BlankUtil.isNotEmpty(param.getSettingDefaultValue()) && !ConvertTypeUtil.checkType(param.getSettingDefaultValue(), param.getSettingValueType()))
            return CommonResult.failed("操作失败，默认值与值类型不匹配！", Boolean.FALSE);

        if ("0".equals(sysSettingId)) {
            // 类型重复检查
            if (BlankUtil.isNotEmpty(typeSetting))
                return CommonResult.failed("该设置类型【" + settingType + "】已存在", Boolean.FALSE);
            // 生成ID
            sysSetting.setSysSettingId(IdWorker.generateId());
            // 当默认值为空时，首次保持与设置相同，避免重复传递相同值过来
            if (BlankUtil.isEmpty(param.getSettingDefaultValue()))
                sysSetting.setSettingDefaultValue(param.getSettingValue());
            // 设置类型分配策略，如果为-1则自动分配类型
            Set<Integer> allSettingType = this.getAllSettingType();
            if ("-1".equals(settingType)) {
                sysSetting.setSettingType(NumberUtil.minMissNumber(allSettingType));
            } else {
                sysSetting.setSettingType(Integer.valueOf(settingType));
            }
            // 当不允许修改禁用选项时，该设置必须启用
            if (BlankUtil.isNotEmpty(param.getAllowDisable()) && !param.getAllowDisable())
                sysSetting.setEnable(Boolean.TRUE);
            LOGGER.info("【用户{}-添加全局设置】-[{}]", pcUserId, sysSetting);
        } else {
            SysSetting setting = this.getById(sysSettingId);
            if (BlankUtil.isEmpty(setting)) return CommonResult.failed("更新失败，ID不存在", Boolean.FALSE);
            // 判断是否允许修改
            if (!setting.getAllowUpdate())
                return CommonResult.failed("抱歉，该设置【" + setting.getSettingKey() + "】禁止被修改", Boolean.FALSE);
            if (BlankUtil.isNotEmpty(typeSetting) && !typeSetting.getSysSettingId().equals(setting.getSysSettingId()))
                return CommonResult.failed("该设置类型【" + settingType + "】已存在", Boolean.FALSE);
            if (!setting.getSettingValueType().equals(param.getSettingValueType()))
                return CommonResult.failed("更新失败，值类型禁止修改", Boolean.FALSE);
            // 判断是否允许禁用
            if (!setting.getAllowDisable() && BlankUtil.isNotEmpty(param.getEnable()) && !param.getEnable())
                return CommonResult.failed("抱歉，该设置【" + setting.getSettingKey() + "】禁止被修改状态", Boolean.FALSE);
            sysSetting.setSysSettingId(setting.getSysSettingId());
            sysSetting.setSettingDefaultValue(null);// 禁止修改默认值
            sysSetting.setSettingType(null);// 三种类型一旦确定则无法修改
            sysSetting.setSettingValueType(null);
            sysSetting.setSettingFormType(null);
            sysSetting.setAllowDisable(null);// 添加之后不能变更
            sysSetting.setAllowUpdate(null);
            sysSetting.setAllowDelete(null);
            LOGGER.info("【用户{}-修改全局设置】-[原：{}]-[新：{}]", pcUserId, setting, param);
        }
        if (BlankUtil.isEmpty(sysSetting.getSettingGroupName())) sysSetting.setSettingGroupName(null);
        // 设置操作者ID
        sysSetting.setPcUserId(pcUserId);
        boolean flag = this.saveOrUpdate(sysSetting);
        if (flag) {
            this.loadAll();// 重新加载缓存
            return CommonResult.success(Boolean.TRUE);
        } else return CommonResult.failed(Boolean.FALSE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> deleteOne(Long sysSettingId, Long pcUserId) {
        // 从数据库查询最新记录
        SysSetting sysSetting = this.getById(sysSettingId);
        if (BlankUtil.isEmpty(sysSetting)) return CommonResult.failed("ID不存在", Boolean.FALSE);
        // 判断是否能删除
        if (!sysSetting.getAllowDelete())
            return CommonResult.failed("抱歉，该设置【" + sysSetting.getSettingKey() + "】禁止被删除", Boolean.FALSE);
        // 删除成功
        boolean result = this.removeById(sysSettingId);
        // 重新加载缓存
        if (result) {
            this.loadAll();
            LOGGER.info("【用户{}-删除全局设置】-[{}]", pcUserId, sysSetting);
            return CommonResult.success("删除成功", Boolean.TRUE);
        } else return CommonResult.failed("删除失败", Boolean.FALSE);
    }

    @Override
    public List<SysSetting> loadAll() {
        // 强制刷新缓存
        List<SysSetting> settingList = this.list();
        redisService.set(this.getRedisKey(), settingList);
        return settingList;
    }

    @Override
    public List<SysSetting> getCacheAll() {
        Object o = redisService.get(this.getRedisKey());
        if (BlankUtil.isNotEmpty(o)) {
            return (List<SysSetting>) o;
        } else {
            return this.loadAll();
        }
    }

    @Override
    public List<SysSetting> getMatchList(List<SettingTypeEnum> typeList) {
        if (BlankUtil.isEmpty(typeList)) return null;
        List<SysSetting> allList = this.getCacheAll();
        List<SysSetting> collect = new ArrayList<>();
        for (SettingTypeEnum item : typeList) {
            List<SysSetting> temp = allList.stream().filter(s -> s.getSettingType().equals(item.getType()))
                                           .collect(Collectors.toList());
            SysSetting sysSetting = null;
            if (BlankUtil.isNotEmpty(temp)) sysSetting = temp.get(0);
            if (BlankUtil.isNotEmpty(sysSetting)) collect.add(sysSetting);
        }
        return collect;
    }

    @Override
    public SysSetting getOneByType(SettingTypeEnum settingType) {
        if (BlankUtil.isEmpty(settingType)) return null;
        List<SysSetting> matchList = this.getMatchList(Collections.singletonList(settingType));
        if (BlankUtil.isNotEmpty(matchList)) return matchList.get(0);
        return null;
    }

    /**
     * 根据设置类型获取值，优先从缓存中读取
     * - 统一使用该方法进行类型转换
     * - 当值为空时，返回默认值
     * - 当被禁用时，返回null
     * <p>
     * {@link SettingValueTypeEnum} 状态类型维护枚举
     *
     * @param settingType 设置类型，枚举
     * @return 值，该值会自动转换类型，直接使用指定的{包装类型}接收即可
     * - 另外当允许禁用状态时，结果必须判空
     */
    @Override
    public <V> V getSettingValue(SettingTypeEnum settingType) {
        List<SysSetting> allList = this.getCacheAll();
        for (SysSetting item : allList) {
            if (!item.getEnable()) continue;
            if (item.getSettingType().equals(settingType.getType())) {
                String settingValue = item.getSettingValue();
                if (BlankUtil.isEmpty(settingValue)) settingValue = item.getSettingDefaultValue();
                return ConvertTypeUtil.convert(settingValue, item.getSettingValueType());
            }
        }
        return null;
    }

    @Override
    public <V> V getSettingValue(SysSetting sysSetting) {
        SettingTypeEnum settingType = EnumTool.getEnum(SettingTypeEnum.class, sysSetting.getSettingType());
        return this.getSettingValue(settingType);
    }

    @Override
    public <V> V getSettingDefaultValue(SysSetting sysSetting) {
        if (BlankUtil.isEmpty(sysSetting)) return null;
        SysSetting setting = this.getOneByType(EnumTool.getEnum(SettingTypeEnum.class, sysSetting.getSettingType()));
        if (BlankUtil.isEmpty(setting)) return null;
        return ConvertTypeUtil.convert(setting.getSettingDefaultValue(), setting.getSettingValueType());
    }

    @Override
    public List<String> getGroupName() {
        return sysSettingMapper.getGroupName();
    }

    @Override
    public Boolean isExist(SettingTypeEnum settingType) {
        SysSetting sysSetting = this.getOneByType(settingType);
        return BlankUtil.isNotEmpty(sysSetting);
    }

    @Override
    public String getGroupNameByType(SettingTypeEnum settingType) {
        SysSetting sysSetting = this.getOneByType(settingType);
        if (BlankUtil.isEmpty(sysSetting)) return null;
        return sysSetting.getSettingGroupName();
    }

    @Override
    public List<SysSetting> getSettingByGroupName(String settingGroupName) {
        List<SysSetting> allSetting = this.getCacheAll();
        return allSetting.stream().filter(s -> settingGroupName.equals(s.getSettingGroupName()))
                         .collect(Collectors.toList());
    }

    @Override
    public Set<Integer> getAllSettingType() {
        return this.getCacheAll().stream().collect(Collectors.groupingBy(SysSetting::getSettingType)).keySet();
    }

    @Override
    public Integer getMaxSettingType() {
        Optional<SysSetting> optional = this.getCacheAll().stream()
                                            .max(Comparator.comparing(SysSetting::getSettingType));
        if (BlankUtil.isNotEmpty(optional) && optional.isPresent()) {
            return optional.get().getSettingType();
        }
        return NumberUtils.INTEGER_ZERO;
    }

    @Override
    public String getRedisKey() {
        return redisConfig.fullKey(redisConfig.getKey().getGlobalSysSetting());
    }
}
