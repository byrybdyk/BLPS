package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/security-admin")
public class SecurityAdminController {
    private final SecurityAdminService securityAdminService;
    private final NewUsersStorageService newUsersStorageService;


    @GetMapping("/requests")
    public ResponseEntity<?> getRequests() {
        return ResponseEntity.ok(newUsersStorageService.getRegistrationRequests());
    }

    @PostMapping("/approve/{requestId}")
    public ResponseEntity<?> approveRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(securityAdminService.approveRequest(requestId));
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(securityAdminService.rejectRequest(requestId));
    }
}
