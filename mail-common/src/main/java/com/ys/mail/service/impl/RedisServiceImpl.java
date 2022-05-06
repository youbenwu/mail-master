package com.ys.mail.service.impl;


import com.ys.mail.model.map.RedisGeoDTO;
import com.ys.mail.service.RedisService;
import com.ys.mail.util.BlankUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作实现类
 *
 * @author 070
 */
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long del(List<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Boolean hSet(String key, String hashKey, Object value, long time) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        return expire(key, time);
    }

    @Override
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Boolean hSetAll(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, time);
    }

    @Override
    public void hSetAll(String key, Map<String, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public void hDel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Long hIncr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Long hDecr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    @Override
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Long sAdd(String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Long lPush(String key, Object value, long time) {
        Long index = redisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
        return index;
    }

    @Override
    public Long lPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public Long lPushAll(String key, Long time, Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public Long keys(String s) {
        Set<String> keys = redisTemplate.keys(s);
        return BlankUtil.isEmpty(keys) ? NumberUtils.LONG_ZERO : redisTemplate.delete(keys);
    }

    @Override
    public Long gAdd(String key, double lat, double lng, Object id) {
        RedisGeoCommands.GeoLocation<Object> geoLocation = new RedisGeoCommands.GeoLocation<>(id, new Point(lat, lng));
        return redisTemplate.opsForGeo().add(key, geoLocation);
    }

    @Override
    public Long gAdd(String key, Map<Object, Point> member) {
        return redisTemplate.opsForGeo().add(key, member);
    }

    @Override
    public Distance gDistance(String key, Object pointA, Object pointB) {
        // TODO 暂未实现
        return null;
    }

    @Override
    public List<RedisGeoDTO> gRadius(String key, double lng, double lat, double radius) {
        GeoOperations<String, Object> geoOperations = redisTemplate.opsForGeo();
        RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs().includeDistance()
                .includeCoordinates().sortAscending();
        Point point = new Point(lng, lat);
        Distance distance = new Distance(radius, Metrics.NEUTRAL);
        Circle circle = new Circle(point, distance);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults = geoOperations.radius(key, circle, geoRadiusCommandArgs);
        return calculateResult(geoResults);

    }

    /**
     * 计算结果
     *
     * @param geoResults 匹配结果
     */
    private List<RedisGeoDTO> calculateResult(GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults) {
        List<RedisGeoDTO> list = new ArrayList<>();
        if (geoResults != null) {
            for (GeoResult<RedisGeoCommands.GeoLocation<Object>> geoResult : geoResults) {
                // 与目标点相距的距离信息
                Distance geoResultDistance = geoResult.getDistance();
                // 该点的信息
                RedisGeoCommands.GeoLocation<Object> geoResultContent = geoResult.getContent();
                // 经纬度
                Point point = geoResultContent.getPoint();
                RedisGeoDTO redisGeoDTO = new RedisGeoDTO();
                redisGeoDTO.setLng(point.getX());
                redisGeoDTO.setLat(point.getY());
                redisGeoDTO.setDistance(geoResultDistance.getValue());
                redisGeoDTO.setId(Long.valueOf(geoResultContent.getName().toString()));
                list.add(redisGeoDTO);
            }
        }
        return list;
    }

    @Override
    public boolean flushDb() {
        String result = redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        });
        return "ok".equals(result);
    }
}
