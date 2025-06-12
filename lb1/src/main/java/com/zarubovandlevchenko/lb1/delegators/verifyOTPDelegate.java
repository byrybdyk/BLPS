package com.zarubovandlevchenko.lb1.delegators;

import com.zarubovandlevchenko.lb1.service.RegisterService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
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
            if(registerService.verifyOTPS(execution.getVariable("phoneNumber").toString(),
                    execution.getVariable("userOTP").toString())){
                System.out.println("Ошибка верификации OTP");
                throw new BpmnError("otpError");
            }

        System.out.println("Верификация OTP завершена");
    }
}