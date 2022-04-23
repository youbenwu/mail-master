package com.ys.mail.service.impl;

import com.ys.mail.model.tree.ProductCategoryTree;
import com.ys.mail.model.tree.SysDistrictTree;
import com.ys.mail.service.SysDistrictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.ys.mail.entity.SysDistrict;
import com.ys.mail.mapper.SysDistrictMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 省市区数据字典表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-04
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SysDistrictServiceImpl extends ServiceImpl<SysDistrictMapper, SysDistrict> implements SysDistrictService {

    @Autowired
    private SysDistrictMapper districtMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.districtTree}")
    private String redisKeyDistrictTree;
    @Value("${redis.expire.district}")
    private Long redisExpireDistrict;

    @Override
    public List<SysDistrictTree> trees() {
        String key = redisDatabase +
                ":" +
                redisKeyDistrictTree;
        List<SysDistrictTree> trees = redisTemplate.opsForList().range(key, 0, 35);
        if(BlankUtil.isEmpty(trees)){
            trees = TreeUtil.toTree(districtMapper.trees(),"disId","parentId","children", SysDistrictTree.class);
            if(! BlankUtil.isEmpty(trees)){
                redisTemplate.opsForList().rightPushAll(key,trees);
                redisTemplate.expire(key,redisExpireDistrict, TimeUnit.DAYS);
            }
        }
        return trees;
    }
}
