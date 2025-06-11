package com.zarubovandlevchenko.lb1.delegators;

import com.zarubovandlevchenko.lb1.dto.SignUpRequest;
import com.zarubovandlevchenko.lb1.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("sendOTPDelegate")
@RequiredArgsConstructor

public class sendOTPDelegate implements JavaDelegate {

    private final RegisterService registerService;

    @Override
    public void execute(DelegateExecution execution) {
        SignUpRequest request = new SignUpRequest(execution.getVariable("name").toString(),
                execution.getVariable("lastname").toString(),
                execution.getVariable("phoneNumber").toString(),
                execution.getVariable("passport").toString());
        System.out.println("Запуск sendOTPDelegate");
        registerService.registrate(request);
        System.out.println("sendOTPDelegate завершен");
    }
}
