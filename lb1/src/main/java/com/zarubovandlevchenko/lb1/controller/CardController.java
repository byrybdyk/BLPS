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
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/user/{id}/cards")
    public List<Card> getUserCards(@PathVariable Long id) {
        return cardService.getUserCards(id);
    }

    @GetMapping("/cards/operations")
    public String getOperationsList() {
        return cardService.getOperationsList();
    }

    @PatchMapping("/cards/{id}/limit")
    public String setLimit(@PathVariable Long id, @RequestBody Double limit) {
        cardService.setLimit(id, limit);
        return "Limit "+ limit + " successfully set for card with id " + id;
    }

    @PatchMapping("/cards/{id}/freeze")
    public String setFreeze(@PathVariable Long id, @RequestBody Boolean isFreeze) {
        cardService.setFreeze(id, isFreeze);
        return "Freeze status " + isFreeze + " successfully set for card with id " + id;
    }

    @PatchMapping("cards/{id}/block")
    public String setBlock(@PathVariable Long id, @RequestBody Boolean isBlocked) {
        cardService.setBlock(id, isBlocked);
        return "Block status " + isBlocked + " successfully set for card with id " + id;
    }

    @PatchMapping("cards/{id}/pin")
    public String setPin(@PathVariable Long id, @RequestBody Integer pin) {
        cardService.setPin(id, pin);
        return "Pin update for card with id " + id;
    }

    @PostMapping("cards/{id}/notifications")
    public String setNotify(@PathVariable Long id, @RequestBody Boolean isNotify) {
        cardService.setNotify(id, isNotify);
        return "Notification status " + isNotify + " successfully set for card with id " + id;
    }
}
