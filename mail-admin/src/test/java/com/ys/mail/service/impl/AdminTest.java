package com.ys.mail.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ys.mail.MailAdminApplication;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.entity.UmsUserBlacklist;
import com.ys.mail.mapper.UmsUserMapper;
import com.ys.mail.override.ChainLinkedHashMap;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.UmsIncomeService;
import com.ys.mail.service.UmsUserBlacklistService;
import com.ys.mail.service.UserManageService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.DateTool;
import com.ys.mail.util.ExcelTool;
import com.ys.mail.util.IdWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-07-15 14:53
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MailAdminApplication.class})
public class AdminTest {

    @Autowired
    private UmsIncomeService umsIncomeService;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private UmsUserMapper umsUserMapper;
    @Autowired
    private UmsUserBlacklistService umsUserBlacklistService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;

    @Test
    public void test() {
        // 查询出所有用户，并获取用户ID
        // List<UmsUser> userList = userManageService.list();
        // List<Long> ids = userList.stream().map(UmsUser::getUserId).collect(Collectors.toList());

        // 获取所有用户的最新余额，并只过滤出余额小于100的用户
        // List<UmsIncome> incomeList = umsIncomeService.selectLatestByUserIds(ids);
        // List<UmsIncome> partIncomeList = incomeList.stream().filter(income -> income.getBalance() >= 100 * 100)
        //                                     .collect(Collectors.toList());

        // 获取这些用户的最近下单时间，并过滤出下单时间超过一个月的用户

        // 判断这些用户是否还持有产品待卖出的
        // 将这些用户的用户数据从数据库中删除，并导出一份Excel表
        // 同时将这批用户的手机号拉入系统黑名单中
    }

    @Test
    public void test2() {
        // 读取文件，获取用户信息列表（id、phone）
        File file = new File("C:\\Users\\MacroBorn\\Desktop\\deleteUser.txt");
        List<String> lines = FileUtil.readLines(file, "UTF-8");
        List<Long> ids = lines.stream().map(Long::valueOf).collect(Collectors.toList());
        // 获取所有用户的详细信息
        LambdaQueryWrapper<UmsUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UmsUser::getUserId, ids);
        List<UmsUser> userList = userManageService.list(wrapper);
        int size = userList.size();
        if (size == 0) {
            System.out.println("没有需要操作数据");
            return;
        }
        System.out.println("需要删除的用户条数" + size);

        // 将手机号拉入系统黑名单中
        List<UmsUserBlacklist> blackList = new ArrayList<>();
        // 构建黑名单对象
        List<Long> generateIds = IdWorker.generateIds(size);
        AtomicInteger index = new AtomicInteger();
        userList.forEach(user -> {
            UmsUserBlacklist black = new UmsUserBlacklist();
            black.setBlId(generateIds.get(index.get()));
            black.setBlName(user.getNickname());
            black.setBlPhone(user.getPhone());
            black.setEnable(true);
            black.setRemark("批量录入");
            black.setPcUserId(1506534705657417728L);
            blackList.add(black);

            // 删除缓存
            String fullKey = redisConfig.fullKey("ums:user:" + user.getPhone());
            redisService.del(fullKey);

            index.getAndIncrement();
        });

        // 删除黑名单缓存
        String fullKey = redisConfig.fullKey(redisConfig.getKey().getUserBlackList());
        redisService.del(fullKey);

        boolean result = umsUserBlacklistService.saveBatch(blackList);
        System.out.println("批量添加黑名单结果" + result);

        // 删除数据库用户信息
        umsUserMapper.deleteByIds(ids);
        System.out.println("删除数据库用户成功");

        // 工作表集合
        Map<String, List<Map<String, Object>>> workbookMap = new HashMap<>(1);
        List<Map<String, Object>> rows = new ArrayList<>();
        userList.forEach(e -> {
            // 将records结果使用Map进行包装，方便自定义显示内容
            ChainLinkedHashMap<String, Object> map = new ChainLinkedHashMap<>();
            map.putObj("用户ID", e.getUserId().toString())
               .putObj("昵称", e.getNickname())
               .putObj("支付宝名称", e.getAlipayName())
               .putObj("支付宝账号", e.getAlipayAccount())
               .putObj("账号状态", "已删除")
               .putObj("删除时间", new Date());
            rows.add(map);
        });
        workbookMap.put("平台删除用户记录", rows);
        // 导出Excel
        ExcelTool.writeExcelToFile(workbookMap, "C:\\Users\\MacroBorn\\Desktop\\平台删除用户记录-" + DateTool.getCurrentDateTime() + ".xlsx");
    }

    @Test
    public void test3() {
        // 获取最新一条记录
        Long userId = 1479079114085371904L;
        List<UmsIncome> umsIncomes = umsIncomeService.selectLatestByUserIds(Collections.singletonList(userId));
        if (BlankUtil.isEmpty(umsIncomes)) {
            return;
        }
        UmsIncome umsIncome = umsIncomes.get(0);
        Long original = 3800000L;
        Long integral = 108324L;
        Long money = original + integral;
        UmsIncome income = UmsIncome.builder()
                                    .incomeId(IdWorker.generateId())
                                    .userId(1479079114085371904L)
                                    .income(0L)
                                    .expenditure(money)
                                    .original(original)
                                    .integral(integral)
                                    .balance(umsIncome.getBalance() - money)
                                    .todayIncome(umsIncome.getTodayIncome())
                                    .allIncome(umsIncome.getAllIncome())
                                    .incomeType(UmsIncome.IncomeType.MINUS_ONE.key())
                                    .detailSource("系统扣除资金")
                                    .incomeNo("")
                                    .orderTradeNo("")
                                    .payType(3)
                                    .remark("手动扣除")
                                    .build();
        boolean save = umsIncomeService.save(income);
        System.out.println(save);
    }

}
