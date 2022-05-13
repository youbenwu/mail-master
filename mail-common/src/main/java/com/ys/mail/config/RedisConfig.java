package com.ys.mail.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Desc Redis 所有缓存配置项，统一管理
 * @Author CRH
 * @Create 2022-03-02 13:51
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    /**
     * 数据库
     */
    private String database;

    /**
     * redis key
     */
    @Autowired
    private RedisKey key;

    /**
     * redis 过期时间
     */
    @Autowired
    private RedisExpire expire;

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "redis.key")
    public class RedisKey {
        private String authCode;
        private String user;
        private String userVerify;
        private String homeAdvertise;
        private String homeSecondProduct;
        private String homeAllSecondProduct;
        private String homeNewestGroupBuy;
        private String homeAllGroupBuy;
        private String homeProductType;
        private String homePage;
        private String districtTree;
        private String sysKdOrderNo;
        private String sysKdCode;
        private String bindAlipay;
        private String pdtCategory;
        private String rongCloudToken;
        private String globalSysSetting;
        private String userBlackList;
        private String pcUser;
        private String menuList;
        private String token;
        private String pcHomePage;
        private String everyDayOrder;
        private String everyMonthOrder;
        private String everyMonthIncome;
        private String monitorServers;
        private String localLockAnn;
        private String inviteUser;
        private String geo;
        private String faceTencent;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "redis.expire")
    public class RedisExpire {
        private Long authCode;
        private Long common;
        private Long homePage;
        private Long district;
        private Long sevenDay;
        private Long anHour;
        private Long minute;
        private Long twenty;
    }

    /**
     * 返回带数据库前缀的key
     *
     * @param key 需要拼接的key
     * @return 完整的key
     */
    public String fullKey(String key) {
        return String.format("%s:%s", this.getDatabase(), key);
    }
}
