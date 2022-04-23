package com.ys.mail.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ghdhj
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;


    public List<String> getRedis(){
        //热搜
        Set<ZSetOperations.TypedTuple<String>> search = redisTemplate.opsForZSet().reverseRangeWithScores("sortSearch", 0, 3);
        ArrayList<String> list = new ArrayList<>();
        for(ZSetOperations.TypedTuple<String> s :search){
            list.add(s.getValue());
        }
        return list;
    }

}
