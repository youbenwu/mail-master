package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.SmsHomeAdvertise;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.mapper.SmsHomeAdvertiseMapper;
import com.ys.mail.model.vo.HomePageVO;
import com.ys.mail.service.*;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-09 16:27
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SmsHomeAdvertiseServiceImpl extends ServiceImpl<SmsHomeAdvertiseMapper, SmsHomeAdvertise> implements SmsHomeAdvertiseService {

    @Autowired
    private SmsHomeAdvertiseMapper homeAdvertiseMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private GroupBuyService groupBuyService;
    @Autowired
    private SmsHomeAdvertiseService homeAdvertiseService;
    @Autowired
    private SmsFlashPromotionService promotionService;
    @Autowired
    private UmsUserService userService;
    @Autowired
    private SysSettingService sysSettingService;

    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.homeAdvertise}")
    private String redisKeyHomeAdvertise;
    @Value("${redis.expire.homePage}")
    private Long redisExpireHomePage;
    @Value("${redis.key.homeSecondProduct}")
    private String redisKeyHomeSecondProduct;
    @Value("${redis.key.homePage}")
    private String redisKeyHomePage;

    @Autowired
    private CosService cosService;

    @Override
    public List<SmsHomeAdvertise> getAllAdvertise() {
        return homeAdvertiseMapper.selectAllAdvertise();
    }

    @Override
    public HomePageVO homePage(Byte cpyType) {
        HomePageVO homePageVO;
        UmsUser currentUser = UserUtil.getCurrentUserOrNull();

        // 拼接redis key
        StringBuilder sb = new StringBuilder();
        sb.append(redisDatabase).append(":").append(redisKeyHomePage);
        if (BlankUtil.isNotEmpty(currentUser)) {
            // 判断是否是高级用户,高级用户才能看到限时秒杀,0普通用户,1高级用户
            if (currentUser.getRoleId().equals(NumberUtils.INTEGER_ZERO)) {
                // 普通用户
                homePageVO = getHomePage(sb.append("Zero"), NumberUtils.INTEGER_ZERO, cpyType);
            } else {
                // 高级用户
                homePageVO = getHomePage(cpyType.equals(NumberUtils.BYTE_ZERO) ? sb.append("ZeroZero") : sb.append("OneOne"), NumberUtils.INTEGER_ONE, cpyType);
            }
        } else {
            // 普通用户
            homePageVO = getHomePage(sb.append("Zero"), NumberUtils.INTEGER_ZERO, cpyType);
        }

        return homePageVO;
    }

    /**
     * .homeGroupBuyVO(HomeGroupBuyVO.builder()
     * .groupSum(userService.count() * 1000)
     * // 团购商品
     * .groupBuyDTOList(groupBuyService.getNewestGroupBuy())
     * .build()
     * )
     *
     * @param sb      拼接字符串
     * @param ite     是否高级用户
     * @param cpyType 轻创营还是卖了吧
     * @return 返回值
     */
    private HomePageVO getHomePage(StringBuilder sb, Integer ite, Byte cpyType) {
        String key = sb.toString();
        HomePageVO homePageVO = (HomePageVO) redisTemplate.opsForValue().get(key);
        if (BlankUtil.isEmpty(homePageVO)) {
            homePageVO = HomePageVO.builder()
                                   // 轮播图
                                   .homeAdvertises(homeAdvertiseService.getAllAdvertise())
                                   // 秒杀商品
                                   .secondProductDTO(promotionService.getSecondProduct(ite, cpyType))
                                   // COS路径前缀
                                   .cosFilePath(cosService.getOssPath())
                                   .build();
            if (!BlankUtil.isEmpty(homePageVO)) {
                redisTemplate.opsForValue().set(key, homePageVO, redisExpireHomePage, TimeUnit.SECONDS);
            }
        }
        return homePageVO;
    }

}
