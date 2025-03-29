package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.model.Card;
import com.zarubovandlevchenko.lb1.model.UserModal;
import com.zarubovandlevchenko.lb1.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserService userService;

    public void setLimit(Long cardId, Double limit) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        if (!validateLimit(limit)) {
            throw new RuntimeException("Limit must be greater than 0");
        }
        card.setLimit(limit);
        cardRepository.save(card);
    }

    private Boolean validateLimit(Double limit) {
        return limit >= 0;
    }

    public void setFreeze(Long cardId, Boolean isFreeze) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        card.setIsFreeze(isFreeze);
        cardRepository.save(card);
    }

    public void setBlock(Long cardId, Boolean isBlocked) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        card.setIsBlocked(isBlocked);
        cardRepository.save(card);
    }

    public void setPin(Long cardId, Integer pin) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        if (!validatePin(pin)) {
            throw new RuntimeException("Pin must be 4 digits");
        }
        card.setPin(pin.toString());
        cardRepository.save(card);
    }

    private boolean validatePin(Integer pin) {
        return ((pin.toString().length() == 4 ) && (pin > 0));
    }

    public void setNotify(Long cardId, Boolean notify) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        card.setNotify(notify);
        cardRepository.save(card);
    }

    public void createCard(UserModal user) {
        Card card = new Card();
        card.setUser(user);
        card.setCardNumber(generateCardNumber());
        card.setExpiredAt(generateExpirationDate());
        card.setCvv(generateCvv());
        card.setPin(generatePin());
        card.setBalance(0.0);
        card.setLimit(0.0);
        card.setIsFreeze(false);
        card.setIsBlocked(false);
        cardRepository.save(card);
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
            throw new RuntimeException("Card ID cannot be null");
        }
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    public List<Card> getAllCards() {
        List<Card> cards = cardRepository.findAll();
        if (cards.isEmpty()) {
            throw new RuntimeException("No cards found");
        }
        return cards;
    }

    public List<Card> getUserCards(Long id) {
        UserModal user = userService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<Card> cards = cardRepository.findAllByUser(user);
        if (cards.isEmpty()) {
            throw new RuntimeException("No cards found for user");
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
