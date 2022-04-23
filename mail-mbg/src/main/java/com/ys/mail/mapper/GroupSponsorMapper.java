package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.GroupSponsor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 拼团发起表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-12-28
 */
public interface GroupSponsorMapper extends BaseMapper<GroupSponsor> {
    /**
     * 查询发起拼团
     * @param groupBuyId 拼团购id
     * @return 返回值
     */
    List<GroupSponsor> selectByGroupBuyId(@Param("groupBuyId") Long groupBuyId);

}
