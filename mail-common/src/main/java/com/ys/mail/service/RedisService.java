package com.ys.mail.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis操作Service
 *
 * @author 070
 */
public interface RedisService {

    /**
     * 保存属性
     *
     * @param key   key值
     * @param value value值
     * @param time  时间
     */
    void set(String key, Object value, long time);

    /**
     * 保存属性
     *
     * @param key   key值
     * @param value value值
     */
    void set(String key, Object value);

    /**
     * 获取属性
     *
     * @param key key值
     * @return 返回值
     */
    Object get(String key);

    /**
     * 删除属性
     *
     * @param key key值
     * @return 返回true或者false
     */
    Boolean del(String key);

    /**
     * 批量删除属性
     *
     * @param keys 集合key
     * @return 返回批量删除的Long类型
     */
    Long del(List<String> keys);

    /**
     * 设置过期时间
     *
     * @param key  key值
     * @param time 过期时间
     * @return 返回true或者false
     */
    Boolean expire(String key, long time);

    /**
     * 获取过期时间
     *
     * @param key key值
     * @return 返回Long长度的时间值
     */
    Long getExpire(String key);

    /**
     * 判断是否有该属性
     *
     * @param key key值
     * @return 返回true或者false
     */
    Boolean hasKey(String key);

    /**
     * 按delta递增
     *
     * @param key   key值
     * @param delta 递增值
     * @return 返回Long类型
     */
    Long incr(String key, long delta);

    /**
     * 按delta递减
     *
     * @param key   ker值
     * @param delta 递减值
     * @return 返回Long类型
     */
    Long decr(String key, long delta);

    /**
     * 获取Hash结构中的属性
     *
     * @param key     key值
     * @param hashKey hashKey值
     * @return 返回值
     */
    Object hGet(String key, String hashKey);

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key     key值
     * @param hashKey hashKey值
     * @param value   value值
     * @param time    时间
     * @return 返回true或者false
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key     key值
     * @param hashKey hashKey值
     * @param value   value值
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 直接获取整个Hash结构
     *
     * @param key key值
     * @return 返回一个Map集合
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 直接设置整个Hash结构
     *
     * @param key  key值
     * @param map  map集合
     * @param time 时间
     * @return 返回值
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

    /**
     * 直接设置整个Hash结构
     *
     * @param key key值
     * @param map map集合
     */
    void hSetAll(String key, Map<String, ?> map);

    /**
     * 删除Hash结构中的属性
     *
     * @param key     key值
     * @param hashKey hashKey值
     */
    void hDel(String key, Object... hashKey);

    /**
     * 判断Hash结构中是否有该属性
     *
     * @param key     key值
     * @param hashKey hashKey值
     * @return 返回true或者false
     */
    Boolean hHasKey(String key, String hashKey);

    /**
     * Hash结构中属性递增
     *
     * @param key     key值
     * @param hashKey hashKey值
     * @param delta   delta值
     * @return 返回Long类型数据
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * Hash结构中属性递减
     *
     * @param key     key值
     * @param hashKey hashKey值
     * @param delta   delta值
     * @return 返回Long类型数据
     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * 获取Set结构
     *
     * @param key key值
     * @return 返回一个Set类型数据结构
     */
    Set<Object> sMembers(String key);

    /**
     * 向Set结构中添加属性
     *
     * @param key    key值
     * @param values values值
     * @return 返回Long类型数据
     */
    Long sAdd(String key, Object... values);

    /**
     * 向Set结构中添加属性
     *
     * @param key    key值
     * @param time   时间
     * @param values values值
     * @return 返回Long类型数据
     */
    Long sAdd(String key, long time, Object... values);

    /**
     * 是否为Set中的属性
     *
     * @param key   key值
     * @param value value值
     * @return 返回true或者false
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取Set结构的长度
     *
     * @param key key值
     * @return 返回Long类型数据
     */
    Long sSize(String key);

    /**
     * 删除Set结构中的属性
     *
     * @param key    key值
     * @param values values值
     * @return 返回Long类型数据
     */
    Long sRemove(String key, Object... values);

    /**
     * 获取List结构中的属性
     *
     * @param key   key值
     * @param start start值
     * @param end   end值
     * @return 返回一个List集合
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取List结构的长度
     *
     * @param key key值
     * @return 返回Long类型的数据
     */
    Long lSize(String key);

    /**
     * 根据索引获取List中的属性
     *
     * @param key   key值
     * @param index Long类型数据
     * @return 返回一个Object类型
     */
    Object lIndex(String key, long index);

    /**
     * 向List结构中添加属性
     *
     * @param key   key值
     * @param value value值
     * @return 返回一个Long类型的数据
     */
    Long lPush(String key, Object value);

    /**
     * 向List结构中添加属性
     *
     * @param key   key值
     * @param value value值
     * @param time  时间
     * @return 返回一个Long类型的数据
     */
    Long lPush(String key, Object value, long time);

    /**
     * 向List结构中批量添加属性
     *
     * @param key    key值
     * @param values values值
     * @return 返回一个Long类型的数据
     */
    Long lPushAll(String key, Object... values);

    /**
     * 向List结构中批量添加属性
     *
     * @param key    key值
     * @param time   时间
     * @param values values值
     * @return 返回一个Long类型的数据
     */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * 从List结构中移除属性
     *
     * @param key   key值
     * @param count count值
     * @param value value值
     * @return 返回一个Long类型的数据
     */
    Long lRemove(String key, long count, Object value);

    /**
     * 批量删除redis缓存
     *
     * @param s 名称
     * @return 返回值
     */
    Long keys(String s);

    /**
     * 清空当前数据库：一般情况下不建议使用
     *
     * @return 返回值
     */
    boolean flushDb();
}