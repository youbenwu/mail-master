package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.model.admin.vo.UmsUserBlackListVO;
import com.ys.mail.model.admin.vo.UserImInfoVO;
import com.ys.mail.model.vo.UserInviteItemDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * app用户表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */
@Mapper
public interface UmsUserMapper extends BaseMapper<UmsUser> {

    /**
     * 查询手底下高级用户数
     *
     * @param userId 用户ID
     * @return 数量
     */
    Integer findAdvancedUsersNumber(@Param("userId") Long userId);

    /**
     * 分页查询数据
     *
     * @param page    页码
     * @param wrapper 条件
     * @return 结果
     */
    IPage<UmsUserBlackListVO> getPage(IPage<UmsUserBlackListVO> page, @Param(Constants.WRAPPER) Wrapper<UmsUserBlackListVO> wrapper);

    /**
     * 根据ID列表获取用户信息
     *
     * @param userIds 用户ID列表
     * @return 列表
     */
    List<UserImInfoVO> getUserImInfo(@Param("userIds") List<String> userIds);

    /**
     * 查询团长所有下级成员的有效秒杀订单消费汇总记录
     *
     * @param parentId  父级ID
     * @param inviteIds 下级ID列表
     * @param isCurrent 是否只查询当月，否则查询全部
     * @return 汇总记录
     */
    List<Map<String, Object>> getInviteOrderCollectByParent(@Param("parentId") Long parentId, @Param("inviteIds") List<Long> inviteIds, @Param("isCurrent") boolean isCurrent);

    /**
     * 根据团长ID获取团员的信息
     *
     * @param parentId 团长ID
     * @return 所有团员个人信息
     */
    List<UserInviteItemDataVO> getUserInviteInfo(@Param("parentId") Long parentId);

}
