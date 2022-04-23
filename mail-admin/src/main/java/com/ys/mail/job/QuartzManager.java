package com.ys.mail.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManager {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();  

    /** 
     * 添加一个定时任务
     *  
     * @param jobName 任务名 
     * @param jobGroupName  任务组名 
     * @param triggerName 触发器名 
     * @param triggerGroupName 触发器组名 
     * @param jobClass  任务 
     * @param cron   时间表达式设置
     */
    public static void addJob(String jobName, String jobGroupName,
            String triggerName, String triggerGroupName, Class jobClass, String cron) {  
        try {  
            Scheduler sched = schedulerFactory.getScheduler();  
            JobDetail jobDetail= JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            sched.scheduleJob(jobDetail, trigger);
            if (!sched.isShutdown()) {
                sched.start();  
            }  
        }catch (SchedulerException e){
            throw new RuntimeException("定时任务不会执行:时间错了{}",e);
        } catch (Exception e) {
            throw new RuntimeException(e);  
        }  
    }  

    /** 
     * 修改一个任务的触发时间
     *  
     * @param jobName 任务名
     * @param jobGroupName 任务组名
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名 
     * @param cron   时间表达式
     */  
    public static void modifyJobTime(String jobName, 
            String jobGroupName, String triggerName, String triggerGroupName, String cron) {  
        try {  
            Scheduler sched = schedulerFactory.getScheduler();  
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);  
            if (trigger == null) {  
                return;  
            }  
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) { 
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                trigger = (CronTrigger) triggerBuilder.build();
                sched.rescheduleJob(triggerKey, trigger);
            }  
        }catch (SchedulerException e){
            throw new RuntimeException("定时任务不会执行:时间错了");
        } catch (Exception e) {
            throw new RuntimeException(e);  
        }  
    }  

    /** 
     * 移除一个任务
     *
     * @param jobName 任务名
     * @param jobGroupName 任务组名
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     */  
    public static void removeJob(String jobName, String jobGroupName,  
            String triggerName, String triggerGroupName) {  
        try {  
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            sched.pauseTrigger(triggerKey);
            sched.unscheduleJob(triggerKey);
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }

    /**
     * @Description:关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

