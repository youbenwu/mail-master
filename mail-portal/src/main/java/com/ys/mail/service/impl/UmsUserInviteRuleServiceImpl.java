package com.ys.mail.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsUserInviteRule;
import com.ys.mail.mapper.UmsUserInviteRuleMapper;
import com.ys.mail.model.dto.UmsUserInviteRuleDTO;
import com.ys.mail.service.UmsUserInviteRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户邀请佣金规则表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@Service
public class UmsUserInviteRuleServiceImpl extends ServiceImpl<UmsUserInviteRuleMapper, UmsUserInviteRule> implements UmsUserInviteRuleService {
    @Autowired
    private UmsUserInviteRuleMapper UmsUserInviteRuleMapper;

    @Override
    public List<UmsUserInviteRule> selectRuleList(UmsUserInviteRuleDTO DTO) {
        return UmsUserInviteRuleMapper.selectRuleList(DTO);
    }

    @Override
    public UmsUserInviteRule selectRuleByAgentLevelType(Integer incomeType, Integer agentLevel) {
        QueryWrapper<UmsUserInviteRule> wrapper = new QueryWrapper<>();

        if (BeanUtil.isNotEmpty(agentLevel)) wrapper.eq("agent_level", agentLevel);
        wrapper.eq("income_type", incomeType)
                .orderByDesc("create_time")
                .last("limit 1");
        return getOne(wrapper);
    }
}
