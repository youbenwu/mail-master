package com.ys.mail.job;

import cn.hutool.core.util.ObjectUtil;
import com.ys.mail.util.BlankUtil;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class UserJob implements Job {

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
            // 2022.2.14  由用户手动触发 回购  20220215 修改状态为:待卖出
        Long flashPromotionId = Long.valueOf(jobExecutionContext.getTrigger().getKey().getGroup());
        if(ObjectUtil.isNotEmpty(flashPromotionId)){
            IncomeCalcu incomeCalcu = SpringContextJobConfig.getBeanByClazz(IncomeCalcu.class);
            incomeCalcu.userIncome(flashPromotionId);
        }
    }
}