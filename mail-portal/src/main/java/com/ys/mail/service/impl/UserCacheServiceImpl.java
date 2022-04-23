package com.ys.mail.service.impl;

import com.ys.mail.entity.UmsUser;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.UserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-09 10:19
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserCacheServiceImpl implements UserCacheService {

    @Autowired
    private RedisService redisService;


    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.user}")
    private String redisKeyUser;
    @Value("${redis.expire.common}")
    private Long redisExpire;
    @Value("${redis.key.authCode}")
    private String redisKeyAuthCode;
    @Value("${redis.expire.authCode}")
    private Long redisExpireAuthCode;

    @Override
    public UmsUser getUser(String phone) {
        String key = redisDatabase + ":" + redisKeyUser + ":" + phone;
        return (UmsUser) redisService.get(key);
    }

    @Override
    public void setUser(UmsUser user) {
        // 默认是给用户缓存设置了24小时
        String key = redisDatabase + ":" + redisKeyUser + ":" + user.getPhone();
        redisService.set(key, user, redisExpire);
    }

    @Override
    public void setAuthCode(String phone, String authCode) {
        // 给验证码设置过期时间为90s
        String key = redisDatabase + ":" + redisKeyAuthCode + ":" + phone;
        redisService.set(key,authCode,redisExpireAuthCode);
    }

    @Override
    public String getAuthCode(String phone) {
        String key = redisDatabase + ":" + redisKeyAuthCode + ":" + phone;
        return (String) redisService.get(key);
    }

    @Override
    public void delUser(String phone) {
        if (! StringUtils.isEmpty(phone)) {
            String key = redisDatabase + ":" + redisKeyUser + ":" + phone;
            redisService.del(key);
        }
    }
}
