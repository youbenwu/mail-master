package com.ys.mail.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.ys.mail.entity.PcMenu;
import com.ys.mail.entity.PcUser;
import com.ys.mail.service.PcMenuService;
import com.ys.mail.service.PcUserCacheService;
import com.ys.mail.service.PcUserService;
import com.ys.mail.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author DT
 * @date 2021-10-20 13:31
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PcUserCacheServiceImpl implements PcUserCacheService {

    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.expire.common}")
    private Long redisExpireCommon;
    @Value("${redis.key.pcUser}")
    private String redisKeyPcUser;
    @Value("${redis.key.menuList}")
    private String redisKeyMenuList;
    @Value("${redis.key.user}")
    private String redisKeyUser;
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    @Value("${redis.key.token}")
    private String redisKeyToken;

    @Autowired
    private RedisService redisService;
    @Autowired
    private PcUserService pcUserService;
    @Autowired
    private PcMenuService pcMenuService;


    @Override
    public PcUser getUser(String username) {
        String key = redisDatabase + ":" + redisKeyPcUser + ":" + username;
        return (PcUser) redisService.get(key);
    }

    @Override
    public void setUser(PcUser user) {
        String key = redisDatabase + ":" + redisKeyPcUser + ":" + user.getPcUserId();
        redisService.set(key,user,redisExpireCommon);
    }

    @Override
    public List<PcMenu> getResourceList(Long pcUserId) {
        String key = redisDatabase + ":" + redisKeyMenuList + ":" + pcUserId;
        return (List<PcMenu>) redisService.get(key);
    }

    @Override
    public void setMenuList(Long pcUserId, List<PcMenu> menus) {
        String key = redisDatabase + ":" + redisKeyMenuList + ":" + pcUserId;
        redisService.set(key,menus,redisExpireCommon);
    }

    @Override
    public void delMenuList(Long pcUserId) {
        String key = redisDatabase + ":" + redisKeyMenuList + ":" + pcUserId;
        redisService.del(key);
    }

    @Override
    public void delRoleMenuList(Long roleId) {
        List<String> pcUserIds = pcUserService.getAllUserIdByRoleId(roleId);
        if(CollUtil.isNotEmpty(pcUserIds)){
            String keyPrefix = redisDatabase + ":" + redisKeyMenuList + ":";
            List<String> keys = pcUserIds.stream().map(pcUserId -> keyPrefix + pcUserId).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delMenuListByResource(Long menuId) {
        List<String> userIds=pcMenuService.getByUserId(menuId);
        if(CollUtil.isNotEmpty(userIds)){
            String keyPrefix = redisDatabase + ":" + redisKeyMenuList + ":";
            List<String> keys = userIds.stream().map(userId -> keyPrefix + userId).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delUser(Long pcUserId) {
        String key = redisDatabase + ":" + redisKeyPcUser + ":" + pcUserId;
        redisService.del(key);
    }

    @Override
    public void setTokenExpiration(String authToken) {
        //TODO 过期时间redis当中存的是s,比如60就是60s
        String key =redisDatabase+":"+redisKeyToken+":"+authToken;
        redisService.set(key,authToken,jwtExpiration);
    }

    @Override
    public boolean delToken(String authToken) {
        String key =redisDatabase+":"+redisKeyToken+":"+authToken;
        return redisService.del(key);
    }
}
