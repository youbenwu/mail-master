package com.ys.mail;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ys.mail.config.CfrConfig;
import com.ys.mail.constant.AlipayConstant;
import com.ys.mail.entity.*;
import com.ys.mail.mapper.SmsFlashPromotionProductMapper;
import com.ys.mail.mapper.UmsUserInviteMapper;
import com.ys.mail.model.po.MebSkuPO;
import com.ys.mail.model.po.UmsUserInviteNumberPO;
import com.ys.mail.model.po.UmsUserInviterPO;
import com.ys.mail.service.PmsProductService;
import com.ys.mail.service.PmsSkuStockService;
import com.ys.mail.service.UmsIncomeService;
import com.ys.mail.service.UmsUserInviteService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.HttpUtil;
import com.ys.mail.util.IdWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 070
 * @date 2020/12/1 10:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MailPortalApplication.class})
public class test {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private UmsUserInviteMapper umsUserInviteMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SmsFlashPromotionProductMapper flashPromotionProductMapper;

    @Autowired
    private UmsUserInviteService userInviteService;

    @Autowired
    private UmsIncomeService incomeService;

    @Value("${prop.staticAccessPath}")
    private String accessPath;
    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${cfr.daWeiHu.appId}")
    private String daWeiHuAppId;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PmsProductService productService;

    @Autowired
    private PmsSkuStockService skuStockService;


    @Test
    public void test() {

        String str = "[M,X,XL,2XL,3XL,4XL]";

    }

    @Test
    public void testReadFile() throws IOException {
       /* String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(AlipayConstant.APP_CERT_PATH)).getPath();
        Thread.currentThread().getContextClassLoader().getResource(AlipayConstant.APP_CERT_PATH).getContent();
        System.out.println(path);*/

        Resource resource = resourceLoader.getResource("classpath:" + AlipayConstant.APP_CERT_PATH);
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String data;
        while ((data = br.readLine()) != null) {
            System.out.println(sb.append(data));
        }
        br.close();
        isr.close();
        is.close();
        System.out.println("-------打印分割符号----------------");
        System.out.println(sb);
    }

    @Test
    public void testReadFile2() throws IOException {
        Resource resource = new ClassPathResource(AlipayConstant.APP_CERT_PATH);
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String data = null;
        while ((data = br.readLine()) != null) {
            System.out.println(data);
        }

        br.close();
        isr.close();
        is.close();
    }

    @Test
    public void testUmsUserInviteMapper() {
        UmsUserInviterPO inviterPO = new UmsUserInviterPO();
        inviterPO.setParentInviteId(1458697747778637824L);
//        inviterPO.setTopInviteId(1458610631027593216L);
        UmsUserInviteNumberPO inviteNumber = umsUserInviteMapper.getInviteNumber(inviterPO);
        assert inviteNumber != null;
        System.out.println("父级邀请人数：" + inviteNumber.getParentInviteNumber());
//        System.out.println("顶级邀请人数：" + inviteNumber.getTopInviteNumber());
    }

   /* @Test
    public void testRuleService() {
        UmsUserInviteRule umsUserInviteRule = umsUserInviteRuleService.selectRuleByAgentLevelType(0, 1);
        System.out.println(umsUserInviteRule);
    }*/


    @Test
    public void test7() {
        File file = new File(accessPath + AlipayConstant.ALIPAY_CERT_PATH);
        System.out.println(file.getPath());
    }

    @Test
    public void test8() {
        // 重测mq
        Set keys = redisTemplate.keys(redisDatabase + ":home:*");
        redisTemplate.delete(keys);
    }

    @Test
    public void test9() {
        CfrConfig cfrConfig = new CfrConfig();
        String url = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/access_token?" +
                "app_id=" + cfrConfig.getDaWeiHuAppId() +
                "&secret=" + cfrConfig.getDaWeiHuAppSecret() +
                "&grant_type=client_credential&version=1.00";
        HttpUtil.get(url);
    }

    @Test
    public void test10() {
        // 设置支付密码
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Test
    public void test11() {
        double dou = 0.005D;
        System.out.println(BigDecimal.valueOf(dou).add(BigDecimal.valueOf(1)));
    }

    @Test
    public void test12() {
        String str = "2022-02-10 20:55:04";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean b = false;
        try {
            long time = sdf.parse(str).getTime();
            b = System.currentTimeMillis() > time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(b);
    }

    @Test
    public void test13(){
        UmsUserInvite userInvite = new UmsUserInvite();
        userInvite.setUserInviteId(IdWorker.generateId());
        userInvite.setUserId(1502509791602413568L);
        userInvite.setParentId(1470567960740499456L);
        boolean save = userInviteService.save(userInvite);
        System.out.println(save);
    }

    @Test
    public void test14(){
        UmsIncome income = UmsIncome.builder()
                .incomeId(IdWorker.generateId())
                .userId(1493146437087793152L)
                .income(100000000L)
                .expenditure(0L)
                .balance(100000000L)
                .todayIncome(100000000L)
                .allIncome(100000000L)
                .incomeType(1)
                .detailSource("系统管理员返还金额")
                .incomeNo("")
                .orderTradeNo("")
                .payType(3)
                .build();
        boolean save = incomeService.save(income);
        System.out.println(save);
    }
    
    @Test
    public void test15(){
        // 调度测试用
        List<PmsProduct> list = productService.selectMebs();
        list.stream().
                filter(Objects::nonNull).
                filter(x -> BlankUtil.isNotEmpty(x.getPrice()))
                .forEach(
                        x -> {
                            x.setMebPrice(new Double(x.getPrice() * 0.5).longValue());
                            x.setPromotionType(2);
                            x.setDisCount(new BigDecimal("0.5"));
                        }
                );
        boolean b = productService.updateBatchById(list);

    }

/*    @Test
    public void test16(){
        //PmsSkuStock byId = skuStockService.getById(1469289771087040512L);
        MebSkuPO po = skuStockService.getBySkuId(1469289771087040512L);
        ///System.out.println(byId);
        System.out.println(po);
    }*/

}
