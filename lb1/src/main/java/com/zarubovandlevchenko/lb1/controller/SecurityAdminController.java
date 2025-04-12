package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.dto.StatusUpdateRequest;
import com.zarubovandlevchenko.lb1.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration-requests")
public class SecurityAdminController {
    private final SecurityAdminService securityAdminService;
    private final NewUsersStorageService newUsersStorageService;

    @GetMapping
    public ResponseEntity<?> getRequests() {
        return ResponseEntity.ok(newUsersStorageService.getRegistrationRequests());
    }

    @PatchMapping("/{requestId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long requestId, @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(securityAdminService.updateRequestStatus(requestId,request.getStatus()));
    }
}
