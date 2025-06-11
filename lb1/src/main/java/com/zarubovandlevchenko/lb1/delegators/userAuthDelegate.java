package com.zarubovandlevchenko.lb1.delegators;

import com.zarubovandlevchenko.lb1.dto.SignInRequest;
import com.zarubovandlevchenko.lb1.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.security.Signature;

@Component("userAuthDelegate")
@RequiredArgsConstructor

public class userAuthDelegate implements JavaDelegate {

    private final AuthService authService;

    @Override
    public void execute(DelegateExecution execution) {

        SignInRequest request = new SignInRequest(execution.getVariable("phoneNumber").toString(),null,execution.getVariable("password").toString(),null, execution.getVariable("phoneNumber").toString());

        if (authService.authenticate(request) == null) {
            throw new BpmnError("authError");
        }
    }

}