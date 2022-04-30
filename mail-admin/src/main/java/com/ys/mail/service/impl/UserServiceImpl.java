package com.ys.mail.service.impl;

import com.ys.mail.entity.User;
import com.ys.mail.mapper.UserMapper;
import com.ys.mail.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-04-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
