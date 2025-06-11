package com.zarubovandlevchenko.lb1.delegators;

import com.zarubovandlevchenko.lb1.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("sendTaskToJiraDelegate")
@RequiredArgsConstructor

public class sendTaskToJiraDelegate implements JavaDelegate {

    private final RegisterService registerService;

    @Override
    public void execute(DelegateExecution execution) {
        String phoneNumber = execution.getVariable("phoneNumber").toString();
        registerService.sendTaskToJira(phoneNumber);
    }
}
