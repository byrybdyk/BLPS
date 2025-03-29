package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.model.Card;
import com.zarubovandlevchenko.lb1.model.UserModal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankWorkerService {
    private final UserService userService;
    private final CardService cardService;

    public void giveCard(Long cardId, Long userId) {

        UserModal user = userService.getUserById(userId);
        Card card = cardService.getCardById(cardId);

        if(user == null) {
            System.out.println("Пользователь не найден");
            return;
        }
        if(card == null) {
            System.out.println("Карта не найдена");
            return;
        }
        if(!card.getUser().getId().equals(user.getId())) {
            System.out.println("Карта не принадлежит пользователю");
            return;
        }
        System.out.println("Карта выдана пользователю " + user.getLastName() + " "+ user.getFirstName() + " с номером карты " + card.getCardNumber());
    }
}
