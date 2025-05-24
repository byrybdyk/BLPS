package com.zarubovandlevchenko.lb1.config;

import com.zarubovandlevchenko.lb1.service.ReportJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail reportJobDetail() {
        return JobBuilder.newJob(ReportJob.class)
                .withIdentity("reportJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger reportJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInHours(1)
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(reportJobDetail())
                .withIdentity("reportTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
