package com.ys.mail.cache;

import com.ys.mail.config.RedisConfig;
import com.ys.mail.service.RedisService;
import com.ys.mail.util.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-13 17:04
 */
@Service
public class RongCloudCache {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;

    public String getUserToken(Long userId) {
        String key = this.getRedisKey(userId);
        Object o = redisService.get(key);
        if (BlankUtil.isNotEmpty(o)) return String.valueOf(o);
        return null;
    }

    public void setUserToken(Long userId, String token) {
        String key = this.getRedisKey(userId);
        redisService.set(key, token, redisConfig.getExpire().getCommon());
    }

    public Boolean delUserToken(Long userId) {
        String key = this.getRedisKey(userId);
        return redisService.del(key);
    }

    public String getRedisKey(Long userId) {
        String fullKey = redisConfig.fullKey(redisConfig.getKey().getRongCloudToken());
        return String.format("%s:%s", fullKey, userId);
    }
}
