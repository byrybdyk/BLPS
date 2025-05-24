package com.zarubovandlevchenko.lb1.service;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportJob implements Job {

    private final ReportService reportService;

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("Quartz job started: генерация отчёта");
        reportService.generateReport();
        System.out.println("Quartz job finished: отчёт сгенерирован");
    }
}
