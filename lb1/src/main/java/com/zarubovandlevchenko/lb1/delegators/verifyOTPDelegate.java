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
        try{
            registerService.verifyOtp(execution.getVariable("phoneNumber").toString(),
                    execution.getVariable("userOTP").toString());
        }catch (Exception e){
            System.out.println("Ошибка верификации OTP: " + e.getMessage());
            throw new BpmnError("otpError");
        }

        System.out.println("Верификация OTP завершена");
    }
}