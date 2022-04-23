package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsUserInviteRule;
import com.ys.mail.model.dto.UmsUserInviteRuleDTO;

import java.util.List;

/**
 * <p>
 * 用户邀请佣金规则表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
public interface UmsUserInviteRuleService extends IService<UmsUserInviteRule> {
    List<UmsUserInviteRule> selectRuleList(UmsUserInviteRuleDTO DTO);

    UmsUserInviteRule selectRuleByAgentLevelType(Integer incomeType, Integer agentLevel);
}
