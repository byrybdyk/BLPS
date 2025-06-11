package com.zarubovandlevchenko.lb1.delegators;

import com.zarubovandlevchenko.lb1.dto.SignInRequest;
import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.service.AuthService;
import com.zarubovandlevchenko.lb1.service.CardService;
import com.zarubovandlevchenko.lb1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("cardActivityDelegate")
@RequiredArgsConstructor

public class cardActivityDelegate implements JavaDelegate {

    private final UserService userService;
    private final CardService cardService;

    @Override
    public void execute(DelegateExecution execution) {
        UserModal user = userService.getUserBylogin(execution.getVariable("phoneNumber").toString());
        List<Card> cards = cardService.getUserCards(user.getId());
        Long cardId = cards.get(0).getCardId();

        String value = execution.getVariable("value").toString();
        String activityType = execution.getVariable("activityType").toString();
        try{
            if (activityType.equals("limit")) {
                cardService.setLimit(cardId, Double.valueOf(value));
            }
            if (activityType.equals("freeze")) {
                cardService.setFreeze(cardId, Boolean.valueOf(value));
            }
            if (activityType.equals("block")) {
                cardService.setBlock(cardId, Boolean.valueOf(value));
            }
            if (activityType.equals("pin")) {
                cardService.setPin(cardId, Integer.valueOf(value));
            }
            if (activityType.equals("notification")) {
                cardService.setNotify(cardId, Boolean.valueOf(value));
            }
        }
        catch (Exception e){
            throw new BpmnError("inputError");
        }

    }

}