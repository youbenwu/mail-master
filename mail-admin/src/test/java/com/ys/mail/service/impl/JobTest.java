package com.ys.mail.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ys.mail.MailAdminApplication;
import com.ys.mail.job.IncomeCalcu;
import com.ys.mail.mapper.OmsOrderMapper;
import com.ys.mail.util.BlankUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author DT
 * @version 1.0
 * @date 2022-01-10 20:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MailAdminApplication.class})
public class JobTest {

    @Autowired
    private IncomeCalcu incomeCalcu;
    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Test
    public void test() throws IOException {
        ///    111
        incomeCalcu.userIncome(1490920507334332416L);
    }

    @Test
    public void test1(){
        List<Long> ids = Arrays.asList(1473885767439880192L,1479392442691227648L,1478245887309385728L);
        Long aLong = omsOrderMapper.queryMoney(ids, "20210112");
        try{
            if(ObjectUtil.isEmpty(aLong)){
                System.out.println("--第一次对---");
            }
            if(BlankUtil.isEmpty(aLong)){
                System.out.println("----第二次对---");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("---来异常了----");
        }finally {
            System.out.println("---最终执行------");
        }



       /* Long money = omsOrderMapper.queryMoney(userids,ymd);
        if(BlankUtil.isEmpty(money)){
            continue;
        }*/
    }
}
