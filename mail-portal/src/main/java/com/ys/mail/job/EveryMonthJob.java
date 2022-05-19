package com.ys.mail.job;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.entity.UmsUserInvite;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.mapper.UmsUserInviteMapper;
import com.ys.mail.mapper.UmsUserMapper;
import com.ys.mail.model.dto.ESGameRecord;
import com.ys.mail.model.dto.ESGroupMaster;
import com.ys.mail.model.dto.GameRecord;
import com.ys.mail.model.dto.GroupMater;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.util.BlankUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liky
 * @date 2022/1/9 13:51
 * @description 每月执行任务
 */
@Component
@EnableScheduling
public class EveryMonthJob {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private UmsUserMapper umsUserMapper;
    @Autowired
    private UmsUserInviteMapper umsUserInviteMapper;
    @Autowired
    private SysSettingService settingService;


    @Scheduled(cron = "0 0 0 1 * ?")
    public void userPlain() {
        List<UmsUser> umsUserList = umsUserMapper.selectList(Wrappers.<UmsUser>lambdaQuery()
                                                                     .eq(UmsUser::getDeleted, 0));
        if (ObjectUtils.isNotEmpty(umsUserList)) {
            for (UmsUser umsUser : umsUserList) {
                userPlain2(umsUser.getUserId().toString());
            }
        }
        masterPlain();
    }

    public void masterPlain() {
        List<UmsUserInvite> umsUserInviteList = umsUserInviteMapper.selectList(Wrappers.<UmsUserInvite>lambdaQuery()
                                                                                       .groupBy(UmsUserInvite::getParentId));
        if (ObjectUtils.isNotEmpty(umsUserInviteList)) {
            List<Long> ids = umsUserInviteList.stream().map(UmsUserInvite::getParentId).distinct()
                                              .collect(Collectors.toList());
            for (Long id : ids) {
                if (BlankUtil.isEmpty(id)) {
                    continue;
                }
                masterPlain2(id.toString());
            }
        }
    }


    @SneakyThrows
    public void masterPlain2(String parentId) {
        GetRequest getRequest = new GetRequest("group_master").id(parentId);
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        IndexRequest indexRequest = new IndexRequest("group_master");

        if (StringUtils.isBlank(sourceAsString)) {
            // 无效用户
            ESGroupMaster esGroupMaster = new ESGroupMaster();
            GroupMater groupMater = new GroupMater();
            groupMater.setDate(Integer.valueOf(new SimpleDateFormat("yyyyMM").format(new Date())));
            Double ratio = settingService.getSettingValue(SettingTypeEnum.four);
            ratio = BlankUtil.isEmpty(ratio) ? 0.002D : ratio;
            groupMater.setRatio(ObjectUtil.equal(ratio, NumberUtils.DOUBLE_ZERO) ? 0.002D : ratio);
            groupMater.setLevel(0);
            esGroupMaster.setEarnings(Collections.singletonList(groupMater));
            indexRequest.id(parentId).source(JSONUtil.toJsonStr(esGroupMaster), XContentType.JSON);
            client.index(indexRequest, RequestOptions.DEFAULT);
            return;
        }
        // 存在的用户
        ESGroupMaster esGroupMaster = JSON.parseObject(sourceAsString, ESGroupMaster.class);
        List<GroupMater> earnings = esGroupMaster.getEarnings();
        List<UmsUserInvite> umsUserInviteList = umsUserInviteMapper.selectList(Wrappers.<UmsUserInvite>lambdaQuery()
                                                                                       .eq(UmsUserInvite::getParentId, parentId));
        List<Long> ids = umsUserInviteList.stream().map(UmsUserInvite::getUserId).collect(Collectors.toList());
        // 上个月活跃用户数
        Integer activeCount = umsUserMapper.selectCount(Wrappers.<UmsUser>lambdaQuery().in(UmsUser::getUserId, ids)
                                                                .eq(UmsUser::getIsActive, 1));
        GroupMater groupMater = earnings.stream().max(Comparator.comparing(GroupMater::getDate)).get();
        Integer level = groupMater.getLevel();
        GroupMater nextGroupMaster = new GroupMater();
        if (activeCount >= 20 + (20 * level)) {
            // 等级增加
            nextGroupMaster.setDate(Integer.valueOf(new SimpleDateFormat("yyyyMM").format(new Date())));
            nextGroupMaster.setRatio(BigDecimal.valueOf(groupMater.getRatio()).add(BigDecimal.valueOf(0.0005D))
                                               .doubleValue());
            nextGroupMaster.setLevel(level + 1);
        } else {
            // 等级不变
            nextGroupMaster.setDate(Integer.valueOf(new SimpleDateFormat("yyyyMM").format(new Date())));
            nextGroupMaster.setRatio(groupMater.getRatio());
            nextGroupMaster.setLevel(level);
        }
        earnings.add(nextGroupMaster);
        indexRequest.source(JSONUtil.toJsonStr(esGroupMaster), XContentType.JSON).id(parentId);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }


    @SneakyThrows
    public void userPlain2(String userId) {
        GetRequest getRequest = new GetRequest("game_record").id(userId);
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        IndexRequest indexRequest = new IndexRequest("game_record");

        if (StringUtils.isBlank(sourceAsString)) {
            // 无效用户
            ESGameRecord esGameRecord = new ESGameRecord();

            GameRecord gameRecord = new GameRecord();
            gameRecord.setDate(Integer.valueOf(new SimpleDateFormat("yyyyMM").format(new Date())));
            // topo: 待改比例  0.005D  用户的
            gameRecord.setRatio(0.005D);
            esGameRecord.setEarnings(Collections.singletonList(gameRecord));

            indexRequest.id(userId).source(JSONUtil.toJsonStr(esGameRecord), XContentType.JSON);
            client.index(indexRequest, RequestOptions.DEFAULT);
            return;
        }
        // 存在的用户
        ESGameRecord esGameRecord = JSON.parseObject(sourceAsString, ESGameRecord.class);
        List<Integer> gameDate = esGameRecord.getGameDate();
        // 每月一号逻辑
        // 1. 判断当前用户是否满足20条数据的要求
        // 2. 拿取当前时间  格式化成  yyyyMMdd 的数字类型  进行比较统计出在这范围内的数量
        LocalDate localDate = LocalDate.now().minusMonths(1);
        Integer first = Integer.valueOf(localDate.with(TemporalAdjusters.firstDayOfMonth())
                                                 .format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Integer last = Integer.valueOf(localDate.with(TemporalAdjusters.lastDayOfMonth())
                                                .format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        System.out.println("开始时间:" + first + "   结束时间:" + last);
        Integer flag = 0;

        GameRecord nextGameRecord = new GameRecord();
        List<GameRecord> earnings = esGameRecord.getEarnings();
        GameRecord gameRecord = earnings.stream().max((Comparator.comparingInt(GameRecord::getDate))).get();

        if (ObjectUtils.isNotEmpty(gameDate) && gameDate.stream().filter(obj -> obj >= first && obj <= last).distinct()
                                                        .collect(Collectors.toList()).size() >= 20) {
            // 上月佣金
            Double ratio = gameRecord.getRatio();
            // 增值佣金范围
            double increasedValue = 0.001D;
            // 获得当前月份
            Integer nexMonth = Integer.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
            // 为es赋值
            nextGameRecord.setDate(nexMonth);
            nextGameRecord.setRatio(BigDecimal.valueOf(ratio).add(BigDecimal.valueOf(increasedValue)).doubleValue());
            earnings.add(nextGameRecord);
            // 更新es 数据
            insertES(indexRequest, esGameRecord, userId);
            // 更新 user 表数据 为 有效用户
            flag = 1;
            UmsUser umsUser = umsUserMapper.selectById(userId);
            umsUser.setIsActive(flag);//有效用户
            umsUserMapper.updateById(umsUser);
        } else {
            Double ratio = gameRecord.getRatio();
            Integer currMonth = Integer.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
            nextGameRecord.setDate(currMonth);
            nextGameRecord.setRatio(ratio);
            earnings.add(nextGameRecord);
            insertES(indexRequest, esGameRecord, userId);
            UmsUser umsUser = umsUserMapper.selectById(userId);
            umsUser.setIsActive(flag);// 无效用户
            umsUserMapper.updateById(umsUser);
        }
    }

    private void insertES(IndexRequest indexRequest, ESGameRecord esGameRecord, String userId) throws IOException {
        indexRequest.source(JSONUtil.toJsonStr(esGameRecord), XContentType.JSON).id(userId);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }


}
