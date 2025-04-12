package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.service.BankWorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BankWorkerController {
    private final BankWorkerService bankWorkerService;

    @GetMapping("/cards/{cardId}/issue")
    public ResponseEntity<?> giveCard(
            @PathVariable Long cardId,
            @RequestBody Long userId) {
        bankWorkerService.giveCard(cardId, userId);
        return ResponseEntity.ok("Card given");
    }
}
