package com.ys.mail.job;

import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MasterJob implements Job {

    /**
     * 团长结算
     */
    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        String[] split = jobExecutionContext.getTrigger().getKey().getGroup().split(",");
        List<Long> flashPromotionIds = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        IncomeCalcu incomeCalcu = SpringContextJobConfig.getBeanByClazz(IncomeCalcu.class);
        incomeCalcu.parentIncome(flashPromotionIds);
    }
}