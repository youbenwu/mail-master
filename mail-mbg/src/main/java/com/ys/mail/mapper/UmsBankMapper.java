package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.UmsBank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户银行卡表,建议后台转1分审核是否成功 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@Mapper
public interface UmsBankMapper extends BaseMapper<UmsBank> {

}
