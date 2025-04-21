package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.exception.CardNotFoundException;
import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
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
            throw new IllegalArgumentException("User cannot be null");
        }
        if(card == null) {
            throw new CardNotFoundException(cardId);
        }
        if(!card.getUser().equals(user.getId())) {
            throw new IllegalArgumentException("Card does not belong to the user");
        }
        System.out.println("Карта выдана пользователю " + user.getLastName() + " "+ user.getFirstName() + " с номером карты " + card.getCardNumber());
    }
}
