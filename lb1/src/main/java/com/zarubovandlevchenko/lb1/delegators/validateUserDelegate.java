package com.zarubovandlevchenko.lb1.delegators;

import com.zarubovandlevchenko.lb1.dto.SignUpRequest;
import com.zarubovandlevchenko.lb1.service.RegisterService;
import com.zarubovandlevchenko.lb1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component("validateUserDelegate")
@RequiredArgsConstructor

public class validateUserDelegate implements JavaDelegate {

    private final UserService userService;

    @Override
    public void execute(DelegateExecution execution) {
        String name = execution.getVariable("name").toString();
        String lastname = execution.getVariable("lastname").toString();
        String passport = execution.getVariable("passport").toString();
        String phoneNumber = execution.getVariable("phoneNumber").toString();

        if(!userService.validateUser(name, passport, phoneNumber, lastname)){
            throw new BpmnError("invalidArgumentsError");
        }
    }
}
