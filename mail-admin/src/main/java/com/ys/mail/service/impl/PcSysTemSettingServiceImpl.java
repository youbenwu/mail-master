package com.ys.mail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.SysTemSetting;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.SysTemSettingMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.PcSysTemSettingService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 系统设置表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PcSysTemSettingServiceImpl extends ServiceImpl<SysTemSettingMapper, SysTemSetting> implements PcSysTemSettingService {

    @Autowired
    private SysTemSettingMapper sysTemSettingMapper;

    @Override
    public CommonResult<Boolean> addBatch(List<SysTemSetting> list) {
        if (BlankUtil.isEmpty(list)) return CommonResult.failed("操作失败，数据为空！");

        List<SysTemSetting> addList = new ArrayList<>();
        List<SysTemSetting> updateList = new ArrayList<>();
        List<Long> longs = IdWorker.generateIds(list.size());
        int index = 0;
        // 统计各类型状态为启用的数量
        Map<Integer, Integer> typeCountMap = new HashMap<>();
        for (SysTemSetting item : list) {
            Integer temType = item.getTemType();
            if (BlankUtil.isEmpty(temType)) return CommonResult.failed("操作失败，类型不能为空！");

            if (BlankUtil.isEmpty(item.getIsTemStatus())) item.setIsTemStatus(Boolean.FALSE);
            if (item.getIsTemStatus()) {
                Integer count = typeCountMap.get(temType);
                if (BlankUtil.isEmpty(count)) count = 0;
                typeCountMap.put(temType, count + 1);
                if (typeCountMap.get(temType) > 1) return CommonResult.failed("操作失败，不能同时添加或修改多条类型相同并为启用的数据！");
            }

            if (BlankUtil.isEmpty(item.getTemSettingId())) {
                item.setTemSettingId(longs.get(index));
                item.setCreateTime(new Date());
                item.setUpdateTime(null);
                addList.add(item);
                index++;
            } else updateList.add(item);
        }

        // 先批量同步状态：将相同类型的数据改为禁用
        List<Integer> typeList = new ArrayList<>(); // 类型ID列表
        typeCountMap.forEach((k, v) -> {
            if (BlankUtil.isNotEmpty(v) && v > 0) {
                typeList.add(k);
            }
        });
        if (BlankUtil.isNotEmpty(typeList)) {
            LambdaUpdateWrapper<SysTemSetting> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(SysTemSetting::getIsTemStatus, 0)
                    .in(SysTemSetting::getTemType, typeList);
            boolean update = this.update(wrapper);
            if (!update) throw new ApiException("修改失败");
        }

        // 再添加到数据库
        if (BlankUtil.isNotEmpty(addList)) {
            int insertBatch = sysTemSettingMapper.insertBatch(addList);
            if (insertBatch != addList.size()) throw new ApiException("添加失败");
        }

        // 更新数据到数据库
        if (BlankUtil.isNotEmpty(updateList)) {
            int updateBatch = sysTemSettingMapper.updateBatch(updateList);
            if (updateBatch < 0) throw new ApiException("修改失败");
        }

        return CommonResult.success(Boolean.TRUE);
    }

    @Override
    public CommonResult<Boolean> delete(Long temSettingId) {
        // 先查询出该ID对应的类型
        SysTemSetting setting = this.getById(temSettingId);
        if (BeanUtil.isNotEmpty(setting)) {
            Integer temType = setting.getTemType();
            // 同种类型不能全部删除，必须保留一条记录
            QueryWrapper<SysTemSetting> wrapper = new QueryWrapper<>();
            wrapper.eq("tem_type", temType);
            int count = this.count(wrapper);
            if (count > 1) {
                boolean result = this.removeById(temSettingId);
                return result ? CommonResult.success("删除成功", Boolean.TRUE) : CommonResult.failed("删除失败");
            }
        }
        return CommonResult.failed("删除失败");
    }

}
