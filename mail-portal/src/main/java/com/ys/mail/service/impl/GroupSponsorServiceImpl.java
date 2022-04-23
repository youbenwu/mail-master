package com.ys.mail.service.impl;

import com.ys.mail.service.GroupSponsorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ys.mail.mapper.GroupSponsorMapper;
import com.ys.mail.entity.GroupSponsor;
/**
 * <p>
 * 拼团发起表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-28
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class GroupSponsorServiceImpl extends ServiceImpl<GroupSponsorMapper, GroupSponsor> implements GroupSponsorService {

}
