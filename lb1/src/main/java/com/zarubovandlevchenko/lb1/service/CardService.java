package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.exception.CardNotFoundException;
import com.zarubovandlevchenko.lb1.exception.InvalidLimitException;
import com.zarubovandlevchenko.lb1.exception.InvalidPinException;
import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserService userService;
    private final TransactionHelper transactionHelper;

    public void setLimit(Long cardId, Double limit) {
        var status = transactionHelper.createTransaction("setLimit");
        try {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new CardNotFoundException(cardId));
            if (!validateLimit(limit)) {
                throw new InvalidLimitException();
            }
            card.setLimit(limit);
            cardRepository.save(card);
            transactionHelper.commit(status);
        }catch(Exception e) {
            transactionHelper.rollback(status);
        }
    }

    private Boolean validateLimit(Double limit) {
        return limit >= 0;
    }


    public void setFreeze(Long cardId, Boolean isFreeze) {
        var status = transactionHelper.createTransaction("setFreeze");
        try {
            Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
            card.setIsFreeze(isFreeze);
            cardRepository.save(card);
            transactionHelper.commit(status);
        }catch(Exception e) {
            transactionHelper.rollback(status);
        }
    }

    public void setBlock(Long cardId, Boolean isBlocked) {
        var status = transactionHelper.createTransaction("setBlock");
        try {
            Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
            card.setIsBlocked(isBlocked);
            cardRepository.save(card);
            transactionHelper.commit(status);
        }catch(Exception e) {
            transactionHelper.rollback(status);
        }
    }

    public void setPin(Long cardId, Integer pin) {
        var status = transactionHelper.createTransaction("setPin");
        try {
            Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
            if (!validatePin(pin)) {
                throw new InvalidPinException();
            }
            card.setPin(pin.toString());
            cardRepository.save(card);
            transactionHelper.commit(status);
        }catch (Exception e) {
            transactionHelper.rollback(status);
        }
    }

    private boolean validatePin(Integer pin) {
        return ((pin.toString().length() == 4 ) && (pin > 0));
    }

    public void setNotify(Long cardId, Boolean notify) {
        var status = transactionHelper.createTransaction("setNotify");
        try {
            Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
            card.setNotify(notify);
            cardRepository.save(card);
            transactionHelper.commit(status);
        }
        catch(Exception e) {
            transactionHelper.rollback(status);
        }
    }

    public Card createCard(UserModal user) {
        var status = transactionHelper.createTransaction("createCard");
        try {
            Card card = new Card();
            card.setUser(user.getId());
            card.setCardNumber(generateCardNumber());
            card.setExpiredAt(generateExpirationDate());
            card.setCvv(generateCvv());
            card.setPin(generatePin());
            card.setBalance(0.0);
            card.setLimit(0.0);
            card.setIsFreeze(false);
            card.setIsBlocked(false);
            cardRepository.save(card);
            transactionHelper.commit(status);
            return card;
        }catch(Exception e) {
            transactionHelper.rollback(status);
            throw e;
        }
    }

    private String generatePin() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pin.append((int) (Math.random() * 10));
        }
        return pin.toString();
    }

    private String generateCvv() {
        StringBuilder cvv = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            cvv.append((int) (Math.random() * 10));
        }
        return cvv.toString();
    }

    private LocalDate generateExpirationDate() {
        return LocalDate.now().plusYears(5);
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                cardNumber.append(" ");
            }
            for (int j = 0; j < 4; j++) {
                cardNumber.append((int) (Math.random() * 10));
            }
        }
        System.out.println("Generated card number: " + cardNumber);
        return cardNumber.toString();
    }

    public Card getCardById(Long cardId) {
        if (cardId == null) {
            throw new IllegalArgumentException("Card ID cannot be null");
        }
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
    }

    public List<Card> getAllCards() {
        List<Card> cards = cardRepository.findAll();
        if (cards.isEmpty()) {
            return null;
        }
        return cards;
    }

    public List<Card> getUserCards(Long id) {
        UserModal user = userService.getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<Card> cards = cardRepository.findAllByUser(user);
        if (cards.isEmpty()) {
            return null;
        }
        return cards;
    }

    public String getOperationsList() {
        String list = "1. Установить лимит\n" +
                "2. Заморозить/разморозить карту\n" +
                "3. Заблокировать карту\n" +
                "4. Установить Pin\n" +
                "5. Включить/выключить уведомления\n";
        return list;
    }
}
