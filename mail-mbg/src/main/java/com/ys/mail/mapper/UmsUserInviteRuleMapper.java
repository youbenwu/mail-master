package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.UmsUserInviteRule;
import com.ys.mail.model.dto.UmsUserInviteRuleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户邀请佣金规则表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@Mapper
public interface UmsUserInviteRuleMapper extends BaseMapper<UmsUserInviteRule> {

    List<UmsUserInviteRule> selectRuleList(@Param("dto") UmsUserInviteRuleDTO dto);
}
