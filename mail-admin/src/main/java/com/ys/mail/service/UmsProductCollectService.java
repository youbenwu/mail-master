package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.UmsProductCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.model.admin.dto.UmsProductCollectDto;

/**
 * <p>
 * 用户收藏产品中间表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-07
 */
public interface UmsProductCollectService extends IService<UmsProductCollect> {

    Page<UmsProductCollectDto> getUmsProductCollectList(Page page);

}
