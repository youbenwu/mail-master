package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.UmsProductCollect;
import com.ys.mail.model.admin.dto.UmsProductCollectDto;

/**
 * <p>
 * 用户收藏产品中间表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-12-07
 */
public interface UmsProductCollectMapper extends BaseMapper<UmsProductCollect> {

    Page<UmsProductCollectDto> getUmsProductCollectList(Page page);
}
