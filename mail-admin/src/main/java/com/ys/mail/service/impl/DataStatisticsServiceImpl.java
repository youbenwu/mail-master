package com.ys.mail.service.impl;

import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.entity.PcReview;
import com.ys.mail.enums.SqlFormatEnum;
import com.ys.mail.mapper.DataStatisticsMapper;
import com.ys.mail.model.admin.vo.statistics.*;
import com.ys.mail.service.DataStatisticsService;
import com.ys.mail.service.OmsOrderService;
import com.ys.mail.service.PcReviewService;
import com.ys.mail.service.RedisService;
import com.ys.mail.util.*;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import com.ys.mail.wrapper.SqlQueryWrapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @Desc 数据统计服务
 * @Author CRH
 * @Create 2022-02-21 15:15
 */
@Service
public class DataStatisticsServiceImpl implements DataStatisticsService {

    @Autowired
    private DataStatisticsMapper dsMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private OmsOrderService omsOrderService;
    @Autowired
    private PcReviewService pcReviewService;

    @Override
    public PcReviewCollectDataVo getReviewDataByDate(String date) {
        return dsMapper.getReviewDataByDate(date);
    }

    @Override
    public Map<String, Object> getAllOrderData(boolean refresh) {
        // 获取key
        String fullKey = redisConfig.fullKey(redisConfig.getKey().getPcHomePage());
        // 获取缓存过期时间
        Long anHour = redisConfig.getExpire().getAnHour();
        // 优先从缓存中读取，如果是强制刷新，则不使用缓存
        Object o = null;
        if (!refresh) {
            o = redisService.get(fullKey);
        }
        return Optional.ofNullable(o).map(e -> (Map<String, Object>) e).orElseGet(() -> {
            Map<String, Object> resultMap = new HashMap<>(5);
            // 从数据库中读取
            List<PcOrderCollectDataVo> allOrderDataList = dsMapper.getOrderDataByDate(null);
            // 获取订单总数
            int totalCount = omsOrderService.count();
            // 获取用户数
            UserCollectDataVo userCollectData = dsMapper.getUserCollectData();
            // 封装结果
            resultMap.put("orderTotal", totalCount);// 总条数
            resultMap.put("orderList", allOrderDataList); // 订单数据列表
            resultMap.put("userData", userCollectData); // 用户数据
            // 存到缓存中
            if (!refresh) {
                ResultUtil.setUnifyParam(resultMap, anHour.intValue());
                redisService.set(fullKey, resultMap, anHour);
            } else {
                ResultUtil.setUnifyParam(resultMap, 0);
            }
            return resultMap;
        });
    }

    @Override
    public Map<String, Object> getOrderDataByDate(String date, boolean refresh) {
        // 获取key
        String fullKey = redisConfig.fullKey(redisConfig.getKey().getEveryDayOrder()) + ":" + date;
        // 获取缓存过期时间
        Long anHour = redisConfig.getExpire().getAnHour();
        // 优先从缓存中读取，如果是强制刷新，则不使用缓存
        Object o = null;
        if (!refresh) {
            o = redisService.get(fullKey);
        }
        return Optional.ofNullable(o).map(e -> (Map<String, Object>) e).orElseGet(() -> {
            // 计算原日期前一天的日期
            String yesterday = DateTool.getYesterday(date);
            // 先获取当天的订单数据
            List<PcOrderCollectDataVo> currentList = dsMapper.getOrderDataByDate(date);
            // 再获取前天的订单数据
            List<PcOrderCollectDataVo> yesterdayList = dsMapper.getOrderDataByDate(yesterday);
            // 最终结果
            List<PcOrderCollectDataVo> resultCollect;
            // 判断昨日数据
            if (BlankUtil.isNotEmpty(yesterdayList)) {
                // 计算环比昨日增长百分率
                List<PcOrderCollectDataVo> collect = currentList.stream().filter(c -> yesterdayList.stream().anyMatch(y -> {
                    if (c.getOrderType().equals(y.getOrderType())) {
                        BigDecimal diffValue = new BigDecimal(c.getOrderNumber() - y.getOrderNumber());
                        c.setSequentialPercent(NumberUtils.DOUBLE_ZERO);
                        BigDecimal yesterdayValue = new BigDecimal(y.getOrderNumber());
                        BigDecimal result = diffValue.divide(yesterdayValue, 4, RoundingMode.HALF_DOWN);
                        c.setSequentialPercent(result.doubleValue());
                        return true;
                    }
                    return false;
                })).collect(Collectors.toList());
            } else {
                // 直接增长100%
                List<PcOrderCollectDataVo> collect = currentList.stream().peek(c -> c.setSequentialPercent(NumberUtils.DOUBLE_ONE)).collect(Collectors.toList());
            }
            // 过滤、填充、排序
            resultCollect = paddingEmptyData(currentList, Arrays.asList(0, 1));
            // 读取订单总数
            SqlLambdaQueryWrapper<OmsOrder> wrapper = new SqlLambdaQueryWrapper<>();
            wrapper.eq(OmsOrder::getDeleteStatus, NumberUtils.INTEGER_ZERO).compareDate(SqlFormatEnum.STRING_DATE_EQ, OmsOrder::getCreateTime, date);
            int totalCount = omsOrderService.count(wrapper);
            // 封装结果
            Map<String, Object> resultMap = new HashMap<>(4);
            resultMap.put("total", totalCount);// 总条数
            resultMap.put("list", resultCollect);// 数据列表
            // 存入缓存中
            if (!refresh) {
                ResultUtil.setUnifyParam(resultMap, anHour.intValue());
                redisService.set(fullKey, resultMap, anHour);
            } else {
                ResultUtil.setUnifyParam(resultMap, 0);
            }
            // 返回结果
            return resultMap;
        });
    }

    @Override
    public Map<String, Object> getSaleOrderDataByYearMonth(String date, boolean refresh) {
        // 获取key
        String fullKey = redisConfig.fullKey(redisConfig.getKey().getEveryMonthOrder()) + ":" + date;
        // 获取缓存过期时间
        Long anHour = redisConfig.getExpire().getAnHour();
        // 优先从缓存中读取，如果是强制刷新，则不使用缓存
        Object o = null;
        if (!refresh) {
            o = redisService.get(fullKey);
        }
        return Optional.ofNullable(o).map(e -> (Map<String, Object>) e).orElseGet(() -> {
            Map<String, Object> resultMap = new HashMap<>(3);
            // 从数据库读取
            List<EveryMonthCollectDataVo> saleOrderData = dsMapper.getSaleOrderDataByYearMonth(date);
            // 封装结果
            resultMap.put("list", saleOrderData); // 秒杀订单数据列表

            // 存入缓存中
            if (!refresh) {
                ResultUtil.setUnifyParam(resultMap, anHour.intValue());
                redisService.set(fullKey, resultMap, anHour);
            } else {
                ResultUtil.setUnifyParam(resultMap, 0);
            }
            return resultMap;
        });
    }

    @Override
    public Map<String, Object> getExIncomeDataByYearMonth(String date, boolean refresh) {
        // 获取key
        String fullKey = redisConfig.fullKey(redisConfig.getKey().getEveryMonthIncome()) + ":" + date;
        // 获取缓存过期时间
        Long anHour = redisConfig.getExpire().getAnHour();
        // 优先从缓存中读取，如果是强制刷新，则不使用缓存
        Object o = null;
        if (!refresh) {
            o = redisService.get(fullKey);
        }
        return Optional.ofNullable(o).map(e -> (Map<String, Object>) e).orElseGet(() -> {
            Map<String, Object> resultMap = new HashMap<>(3);
            // 从数据库读取
            List<EveryMonthCollectDataVo> incomeData = dsMapper.getExIncomeDataByYearMonth(date);
            // 获取当天日期
            String todayNow = DateTool.getTodayNow();
            // 根据当天日期获取待审核的金额
            SqlQueryWrapper<PcReview> queryWrapper = new SqlQueryWrapper<>();
            queryWrapper.eq("deleted", NumberUtils.INTEGER_ZERO)
                    .eq("review_state", NumberUtils.INTEGER_ZERO)
                    .compareDate(SqlFormatEnum.STRING_DATE_EQ, "create_time", todayNow)
                    .select("IFNULL(SUM(review_money),0) reviewMoney");
            Map<String, Object> reviewMap = pcReviewService.getMap(queryWrapper);
            AtomicLong reviewMoney = new AtomicLong(0L);
            if (BlankUtil.isNotEmpty(reviewMap)) {
                BigDecimal temp = (BigDecimal) reviewMap.get("reviewMoney");
                reviewMoney.set(temp.longValue());
            }
            // 减去当天未提现的金额
            List<EveryMonthCollectDataVo> collect = incomeData.stream().peek(ex -> {
                if (ex.getDay().equals(todayNow)) {
                    Long actualEx = ex.getMoney() - reviewMoney.get();
                    ex.setMoney(actualEx);
                }
            }).collect(Collectors.toList());
            // 封装结果
            resultMap.put("list", incomeData); // 提现数据列表
            // 存入缓存中
            if (!refresh) {
                ResultUtil.setUnifyParam(resultMap, anHour.intValue());
                redisService.set(fullKey, resultMap, anHour);
            } else {
                ResultUtil.setUnifyParam(resultMap, 0);
            }
            return resultMap;
        });
    }

    @Override
    public Map<String, Object> getUserInviteLadderList(int pageNum, int pageSize) {
        // 获取所有结果集
        List<InviteLadderListVo> ladderListList = dsMapper.getAllUserInviteCount();
        // 分页处理
        return StreamPageUtil.getPage(ladderListList, pageNum, pageSize);
    }

    /**
     * 填充空数据，并排序
     *
     * @param currentList     原列表
     * @param includeTypeList 需要的类型
     * @return 返回所有类型的数据，包含空对象
     */
    private List<PcOrderCollectDataVo> paddingEmptyData(List<PcOrderCollectDataVo> currentList, List<Integer> includeTypeList) {
        List<PcOrderCollectDataVo> resultList = currentList.stream().filter(e -> includeTypeList.contains(e.getOrderType())).collect(Collectors.toList());
        List<Integer> typeCollect = resultList.stream().map(PcOrderCollectDataVo::getOrderType).collect(Collectors.toList());
        List<Integer> missNumberList = NumberUtil.minMissNumberList(typeCollect, 0, 1).stream().filter(includeTypeList::contains).collect(Collectors.toList());
        // 构造完整类型数据
        missNumberList.stream().map(n -> {
            PcOrderCollectDataVo tempBuild = PcOrderCollectDataVo.builder().orderType(n).orderNumber(NumberUtils.INTEGER_ZERO).orderAmount(NumberUtils.LONG_ZERO).sequentialPercent(NumberUtils.DOUBLE_ZERO).build();
            resultList.add(tempBuild);
            return true;
        }).collect(Collectors.toList());
        // 排序
        return resultList.stream().sorted(Comparator.comparing(PcOrderCollectDataVo::getOrderType)).collect(Collectors.toList());
    }

}
