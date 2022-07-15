package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.entity.UmsUserBlacklist;
import com.ys.mail.exception.BusinessException;
import com.ys.mail.mapper.UmsUserBlacklistMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.UmsUserBlackListParam;
import com.ys.mail.model.admin.query.UserBlackListQuery;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.UmsUserBlacklistService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-01-30
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UmsUserBlacklistServiceImpl extends ServiceImpl<UmsUserBlacklistMapper, UmsUserBlacklist> implements UmsUserBlacklistService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;

    @Override
    public Boolean isOnBlackList(String phone) {
        // 查询缓存，如果不存在则访问数据库
        Object o = redisService.get(this.getRedisKey());
        List<UmsUserBlacklist> list;
        if (BlankUtil.isNotEmpty(o)) {
            list = (List<UmsUserBlacklist>) o;
        } else {
            list = this.list();
            // 添加到缓存
            redisService.set(this.getRedisKey(), list, redisConfig.getExpire().getCommon());
        }
        // 遍历集合判断
        for (UmsUserBlacklist item : list) {
            if (item.getBlPhone().equals(phone) && item.getEnable()) return true;
        }
        return false;
    }

    @Override
    public CommonResult<Page<UmsUserBlacklist>> getPage(UserBlackListQuery query) {
        Page<UmsUserBlacklist> page = new Page<>(query.getPageNum(), query.getPageSize());
        String beginTime = query.getBeginTime();
        String endTime = query.getEndTime();
        QueryWrapper<UmsUserBlacklist> wrapper = new QueryWrapper<>();
        if (BlankUtil.isNotEmpty(query.getBlPhone())) wrapper.like("bl_phone", query.getBlPhone());
        if (BlankUtil.isNotEmpty(query.getBlName())) wrapper.like("bl_name", query.getBlName());
        if (BlankUtil.isNotEmpty(query.getEnable())) wrapper.eq("is_enable", query.getEnable());
        if (!BlankUtil.isEmpty(beginTime) && !BlankUtil.isEmpty(endTime)) {
            if (beginTime.compareTo(endTime) > 0) return CommonResult.failed("开始时间不能大于结束时间", null);
            wrapper.between("date_format( create_time,'%Y-%m-%d %T')", beginTime, endTime);
        }
        Page<UmsUserBlacklist> blacklistPage = this.page(page, wrapper);
        return CommonResult.success(blacklistPage);
    }

    @Override
    public CommonResult<UmsUserBlacklist> getOne(Long blId) {
        UmsUserBlacklist object = this.getById(blId);
        return BlankUtil.isNotEmpty(object) ? CommonResult.success(object) : CommonResult.failed("查询失败");
    }

    @Override
    public CommonResult<Boolean> update(UmsUserBlackListParam param, Long pcUserId) {
        UmsUserBlacklist newObject = new UmsUserBlacklist();
        BeanUtils.copyProperties(param, newObject);
        String blId = param.getBlId();
        // 手机号重复查询
        QueryWrapper<UmsUserBlacklist> wrapper = new QueryWrapper<>();
        wrapper.eq("bl_phone", newObject.getBlPhone());
        UmsUserBlacklist one = this.getOne(wrapper);
        if ("0".equals(blId)) {
            if (BlankUtil.isNotEmpty(one)) return CommonResult.failed("手机号已存在");
            newObject.setBlId(IdWorker.generateId());
            if (BlankUtil.isEmpty(newObject.getEnable())) newObject.setEnable(Boolean.TRUE);
        } else {
            UmsUserBlacklist oldObject = this.getById(blId);
            if (BlankUtil.isEmpty(oldObject)) return CommonResult.failed("更新失败，ID不存在");
            if (BlankUtil.isNotEmpty(one) && !one.getBlId().equals(oldObject.getBlId()))
                return CommonResult.failed("手机号已存在");
            newObject.setBlId(oldObject.getBlId());
        }
        newObject.setPcUserId(pcUserId);
        boolean flag = this.saveOrUpdate(newObject);
        // 删除缓存
        redisService.del(this.getRedisKey());
        return flag ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }

    @Override
    public CommonResult<Boolean> deleteOne(Long blId) {
        boolean flag = this.removeById(blId);
        // 删除缓存
        redisService.del(this.getRedisKey());
        return flag ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }

    public String getRedisKey() {
        return redisConfig.fullKey(redisConfig.getKey().getUserBlackList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancelAccount(UmsUser user) {
        // 手机号加入到黑名单,加入到黑名单,已经是黑名单了,和需要删除redis中key的缓存,可能存在手机号,但是is_enable为false
        // 存在,不存在,null
        LambdaQueryWrapper<UmsUserBlacklist> wrapper = Wrappers.<UmsUserBlacklist>lambdaQuery()
                                                               .eq(UmsUserBlacklist::getBlPhone, user.getPhone());
        UmsUserBlacklist one = this.getOne(wrapper);
        UmsUserBlacklist blacklist = new UmsUserBlacklist();
        if (BlankUtil.isEmpty(one)) {
            blacklist.setBlId(IdWorker.generateId());
            blacklist.setBlPhone(user.getPhone());
            blacklist.setBlName(BlankUtil.isEmpty(user.getAlipayName()) ? user.getNickname() : user.getAlipayName());
        } else {
            if (!one.getEnable()) {
                BeanUtils.copyProperties(one, blacklist);
                blacklist.setEnable(Boolean.TRUE);
            }
        }
        blacklist.setRemark("用户注销账号");
        boolean response = this.saveOrUpdate(blacklist);
        redisService.del(this.getRedisKey());
        return response;
    }

    @Override
    public void checkPhone(String phone) {
        if (BlankUtil.isNotEmpty(phone)) {
            Boolean onBlackList = this.isOnBlackList(phone);
            if (onBlackList) {
                log.warn("【黑名单手机号】- {}", phone);
                throw new BusinessException("请求失败");
            }
        }
    }
}
