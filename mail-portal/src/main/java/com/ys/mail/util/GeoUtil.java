package com.ys.mail.util;

import com.ys.mail.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-04-30 09:44
 * @since 1.0
 */
@Component
public class GeoUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;

    /**
     * 将经纬度信息添加到redis中
     *
     * @param certId    标识
     * @param longitude 经度
     * @param latitude  纬度
     */
    public void geoAdd(String geoKey, Object certId, double longitude, double latitude) {
        GeoOperations geoOperations = redisTemplate.opsForGeo();
        Point point = new Point(longitude, latitude);
        RedisGeoCommands.GeoLocation geoLocation = new RedisGeoCommands.GeoLocation(certId, point);
        geoOperations.add(geoKey, geoLocation);
    }

    /**
     * 两个人之间的距离
     *
     * @param certId1
     * @param certId2
     * @return
     */
    public double distanceBetween(String geoKey, Object certId1, Object certId2) {
        GeoOperations geoOperations = redisTemplate.opsForGeo();
        Distance distance = geoOperations.distance(geoKey, certId1, certId2);
        return distance.getValue();
    }

    /**
     * 查询距离某个人指定范围内的人，包括距离多少米
     *
     * @param certId
     * @param distance
     * @return
     */
    public Map<Object, Double> distanceInclude(String geoKey, Object certId, double distance) {
        Map<Object, Double> map = new LinkedHashMap<>();
        GeoOperations<Object, Object> geoOperations = redisTemplate.opsForGeo();
        RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults = geoOperations.radius(geoKey, certId, new Distance(distance), geoRadiusCommandArgs.includeDistance());
        // calculateResult(map, geoResults);
        return map;
    }
}
