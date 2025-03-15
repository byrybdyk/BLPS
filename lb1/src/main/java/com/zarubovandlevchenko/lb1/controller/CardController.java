package com.zarubovandlevchenko.lb1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.zarubovandlevchenko.lb1.service.CardService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/setLimit")
    public ResponseEntity<Void> setLimit(@RequestBody Long cardId, @RequestBody BigDecimal limit) {
        cardService.setLimit(cardId, limit);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setFreeze")
    public ResponseEntity<Void> setFreeze(@RequestBody Long cardId, @RequestBody Boolean isFreeze) {
        cardService.setFreeze(cardId, isFreeze);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setBlock")
    public ResponseEntity<Void> setBlock(@RequestBody Long cardId, @RequestBody Boolean isBlocked) {
        cardService.setBlock(cardId, isBlocked);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/setPin")
    public ResponseEntity<Void> setPin(@RequestBody Long cardId, @RequestBody Integer pin) {
        cardService.setPin(cardId, pin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setNotify")
    public ResponseEntity<Void> setNotify(@RequestBody Long cardId, @RequestBody Boolean notify) {
        cardService.setNotify(cardId, notify);
        return ResponseEntity.ok().build();
    }
}
