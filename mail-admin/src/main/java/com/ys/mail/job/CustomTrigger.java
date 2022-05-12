package com.ys.mail.job;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.mapper.SmsFlashPromotionMapper;
import com.ys.mail.model.dto.TemporaryWorkerMasters;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class CustomTrigger {

    public static String JOB_NAME = "动态任务调度";
    public static String TRIGGER_NAME = "动态任务触发器";
    public static String TRIGGER_MASTER_NAME = "MASTER动态任务触发器";
    public static String JOB_GROUP_MASTER = "JOB_GROUP_MASTER";
    public static String JOB_GROUP_USER = "JOB_GROUP_USER";

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomTrigger.class);


    @Autowired
    private SmsFlashPromotionMapper flashPromotionMapper;


    public void resetJob() throws ParseException {
        QuartzManager.shutdownJobs();
        trigger();
        everyDay();
    }

    /**
     * 每场结算
     */
    @PostConstruct
    public void trigger() throws ParseException {
        List<SmsFlashPromotion> smsFlashPromotions = flashPromotionMapper.selectList(Wrappers.<SmsFlashPromotion>lambdaQuery()
                .eq(SmsFlashPromotion::getPublishStatus, "1")
                .gt(SmsFlashPromotion::getEndTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .orderByAsc(SmsFlashPromotion::getStartTime)
        );
        if (ObjectUtils.isEmpty(smsFlashPromotions)) {
            return;
        }
        for (SmsFlashPromotion smsFlashPromotion : smsFlashPromotions) {
            Long flashPromotionId = smsFlashPromotion.getFlashPromotionId();
            Date endTime = smsFlashPromotion.getEndTime();
            try {
                String cron = getUserCron(endTime);
                QuartzManager.addJob(JOB_NAME, JOB_GROUP_USER + flashPromotionId, TRIGGER_NAME + flashPromotionId, flashPromotionId.toString(), UserJob.class, cron);
            LOGGER.info("用户秒杀场次id:{},开场时间:{}",smsFlashPromotion.getFlashPromotionId(),cron);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 每日结算
     */
    @PostConstruct
    public void everyDay() throws ParseException {
        List<TemporaryWorkerMasters> smsFlashPromotions = flashPromotionMapper.findFlashPromotionId();
        if (ObjectUtils.isEmpty(smsFlashPromotions)) {
            return;
        }
        for (TemporaryWorkerMasters smsFlashPromotion : smsFlashPromotions) {
            List<Long> flashPromotionId = smsFlashPromotion.getFlashPromotionId();
            String join = StringUtils.join(flashPromotionId.toArray(), ",");
            Date endTime = smsFlashPromotion.getDate();
            try {
                String cron = getMasterCron(endTime);
                QuartzManager.addJob(JOB_NAME, JOB_GROUP_MASTER + join, TRIGGER_MASTER_NAME + join, join, MasterJob.class, cron);
                LOGGER.info("团长秒杀场次id:{},开场时间:{}",smsFlashPromotion.getFlashPromotionId(),cron);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    public static String getUserCron(Date date) {
        DateTime dateTime = new DateTime(date);
        DateTime dateNew = dateTime.offset(DateField.MINUTE, 10);
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return formatDateByPattern(dateNew, dateFormat);
    }

    public static String getMasterCron(Date date) {
        String dateFormat = "00 45 23 dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }

}