package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.model.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.zarubovandlevchenko.lb1.service.CardService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/user/{id}/cards")
    public ResponseEntity<List<Card>> getUserCards(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getUserCards(id));
    }

    @GetMapping("/cards/operations")
    public ResponseEntity<String> getOperationsList() {
        return ResponseEntity.ok(cardService.getOperationsList());
    }

    @PatchMapping("/cards/{id}/limit")
    public ResponseEntity<Void> setLimit(@PathVariable Long id, @RequestBody Double limit) {
        cardService.setLimit(id, limit);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/cards/{id}/freeze")
    public ResponseEntity<Void> setFreeze(@PathVariable Long id, @RequestBody Boolean isFreeze) {
        cardService.setFreeze(id, isFreeze);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("cards/{id}/block")
    public ResponseEntity<Void> setBlock(@PathVariable Long id, @RequestBody Boolean isBlocked) {
        cardService.setBlock(id, isBlocked);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("cards/{id}/pin")
    public ResponseEntity<Void> setPin(@PathVariable Long id, @RequestBody Integer pin) {
        cardService.setPin(id, pin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("cards/{id}/notifications")
    public ResponseEntity<Void> setNotify(@PathVariable Long id, @RequestBody Boolean isNotify) {
        cardService.setNotify(id, isNotify);
        return ResponseEntity.ok().build();
    }
}
