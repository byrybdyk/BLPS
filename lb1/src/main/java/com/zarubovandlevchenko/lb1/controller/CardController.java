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

    @GetMapping("/user/{id}/cards")
    public ResponseEntity<List<Card>> getUserCards(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getUserCards(id));
    }

    @GetMapping("/operationsList")
    public ResponseEntity<String> getOperationsList() {
        return ResponseEntity.ok(cardService.getOperationsList());
    }

    @PostMapping("/{id}/setLimit/{limit}")
    public ResponseEntity<Void> setLimit(@PathVariable Long id, @PathVariable Double limit) {
        cardService.setLimit(id, limit);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/setFreeze/{isFreeze}")
    public ResponseEntity<Void> setFreeze(@PathVariable Long id, @PathVariable Boolean isFreeze) {
        cardService.setFreeze(id, isFreeze);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/setBlock/{isBlocked}")
    public ResponseEntity<Void> setBlock(@PathVariable Long id, @PathVariable Boolean isBlocked) {
        cardService.setBlock(id, isBlocked);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{id}/setPin/{pin}")
    public ResponseEntity<Void> setPin(@PathVariable Long id, @PathVariable Integer pin) {
        cardService.setPin(id, pin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/setNotify/{isNotify}")
    public ResponseEntity<Void> setNotify(@PathVariable Long id, @PathVariable Boolean isNotify) {
        cardService.setNotify(id, isNotify);
        return ResponseEntity.ok().build();
    }
}
