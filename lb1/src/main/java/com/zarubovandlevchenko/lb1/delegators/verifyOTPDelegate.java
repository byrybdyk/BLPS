package com.zarubovandlevchenko.lb1.delegators;

import com.zarubovandlevchenko.lb1.service.RegisterService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("verifyOTPDelegate")
@RequiredArgsConstructor
public class verifyOTPDelegate implements JavaDelegate {
    private final RegisterService registerService;
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("Выполняется верификация OTP");
        registerService.verifyOtp(execution.getVariable("phoneNumber").toString(),
                execution.getVariable("userOTP").toString());
        System.out.println("Верификация OTP завершена");
    }
}