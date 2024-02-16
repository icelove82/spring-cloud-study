package com.ymh.ms.customer.other;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleTaskConfig {

    @Scheduled(fixedDelay = 900000)
    public void scheduleFixedDelayTask() throws InterruptedException {
        System.out.println(
                "Task01 : Fixed delay task - " + System.currentTimeMillis() / 1000);
        Thread.sleep(900000);
    }

    @Scheduled(fixedDelay = 900000)
    public void scheduleFixedDelayTask2() throws InterruptedException {
        System.out.println(
                "Task02 : Fixed delay task - " + System.currentTimeMillis() / 1000);
        Thread.sleep(900000);
    }

    @Scheduled(cron = "0 15 10 * * ?")
    public void scheduleTaskUsingCronExpression() {
        // 调度该任务在每日上午 10:15 执行

        long now = System.currentTimeMillis() / 1000;
        System.out.println(
                "Daily Task schedule tasks using cron jobs - " + now);
    }
}
