package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.dto.StatusUpdateRequest;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration-requests")
public class SecurityAdminController {
    private final SecurityAdminService securityAdminService;
    private final NewUsersStorageService newUsersStorageService;

    @GetMapping
    public Map<Long, UserModal> getRequests() {
        return newUsersStorageService.getRegistrationRequests();
    }

    @PatchMapping("/{requestId}")
    public String updateStatus(@PathVariable Long requestId, @RequestBody StatusUpdateRequest request) {
        return securityAdminService.updateRequestStatus(requestId, request.getStatus());
    }
}
