package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.service.BankWorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BankWorkerController {
    private final BankWorkerService bankWorkerService;

    @GetMapping("/giveCard/{cardId}/user/{userId}")
    public ResponseEntity<?> giveCard(@PathVariable Long cardId, @PathVariable Long userId) {
        bankWorkerService.giveCard(cardId, userId);
        return ResponseEntity.ok("Card given");
    }
}
