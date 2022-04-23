package com.ys.mail.service.impl;

import com.ys.mail.entity.SmsHomeAdvertise;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.GroupBuyDTO;
import com.ys.mail.model.dto.GroupBuyInfoDTO;
import com.ys.mail.service.GroupBuyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.util.BlankUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.ys.mail.mapper.GroupBuyMapper;
import com.ys.mail.entity.GroupBuy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 团购 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-13
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class GroupBuyServiceImpl extends ServiceImpl<GroupBuyMapper, GroupBuy> implements GroupBuyService {

    @Autowired
    private GroupBuyMapper groupBuyMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.homeNewestGroupBuy}")
    private String redisKeyHomeNewestGroupBuy;
    @Value("${redis.expire.homePage}")
    private Long redisExpireHomePage;
    @Value("${redis.key.homeAllGroupBuy}")
    private String redisKeyHomeAllGroupBuy;

    @Override
    public List<GroupBuyDTO> getNewestGroupBuy() {
        return groupBuyMapper.selectNewestGroupBuy();
    }

    @Override
    public List<GroupBuyDTO> getAllGroupBuy(Long pageNum) {
        // TODO 查询出来的全部放入redis中,第一页,第二页,第三页 先全部查找出来,联调的时候再改
        // 缓存存入7天
        String key = redisDatabase + ":" + redisKeyHomeAllGroupBuy;
        boolean equals = pageNum.equals(NumberUtils.LONG_ZERO);
        List<GroupBuyDTO> dtoList = equals ? redisTemplate.opsForList().range(key, 0, 20) : redisTemplate.opsForList().range(key, pageNum, 20);
        if(BlankUtil.isEmpty(dtoList) && equals){
            dtoList = groupBuyMapper.selectAllGroupBuy();
            if(! BlankUtil.isEmpty(dtoList)){
                ListOperations<String, GroupBuyDTO> lo = redisTemplate.opsForList();
                lo.rightPushAll(key,dtoList);
                redisTemplate.expire(key,redisExpireHomePage, TimeUnit.SECONDS);
            }
        }
        return dtoList;
    }

    @Override
    public GroupBuyInfoDTO groupBuyInfo(Long productId) {
        // TODO 未写完,待修改
        return groupBuyMapper.selectByProductIdInfo(productId);
    }
}
