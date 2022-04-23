package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ys.mail.service.GroupTuxedoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.mail.mapper.GroupTuxedoMapper;
import com.ys.mail.entity.GroupTuxedo;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 参团列表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-28
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class GroupTuxedoServiceImpl extends ServiceImpl<GroupTuxedoMapper, GroupTuxedo> implements GroupTuxedoService {

}
