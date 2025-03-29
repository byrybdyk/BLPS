package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.model.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.zarubovandlevchenko.lb1.service.CardService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @PostMapping("/setLimit")
    public ResponseEntity<Void> setLimit(@RequestBody Long cardId, @RequestBody Double limit) {
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
