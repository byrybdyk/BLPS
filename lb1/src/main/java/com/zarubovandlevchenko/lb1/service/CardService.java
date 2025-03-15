package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.model.Card;
import com.zarubovandlevchenko.lb1.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    public void setLimit(Long cardId, BigDecimal limit) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        if (!validateLimit(limit)) {
            throw new RuntimeException("Limit must be greater than 0");
        }
        card.setLimit(limit);
        cardRepository.save(card);
    }

    private Boolean validateLimit(BigDecimal limit) {
        return limit.compareTo(BigDecimal.ZERO) > 0;
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
}
