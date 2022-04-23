package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.UmsUserInvite;
import com.ys.mail.model.dto.TemporaryWorkers;
import com.ys.mail.model.po.UmsUserInviteNumberPO;
import com.ys.mail.model.po.UmsUserInviterPO;
import com.ys.mail.model.vo.UserInviteItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户邀请信息表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@Mapper
public interface UmsUserInviteMapper extends BaseMapper<UmsUserInvite> {

    UmsUserInviterPO getParents(@Param("userId") Long userId);

    UmsUserInviteNumberPO getInviteNumber(@Param("inviterPO") UmsUserInviterPO inviterPO);

    List<UserInviteItemVO> getInviteUser(@Param("userId") Long userId,@Param("userInviteId") Long userInviteId,@Param("pageSize") Integer pageSize);

    List<UserInviteItemVO> getPcInviteUser(@Param("userName") String userName,@Param("parentName") String parentName);

    List<TemporaryWorkers> findUserIdsByParentId();

}
