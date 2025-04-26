package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.dto.StatusUpdateRequest;
import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.repository.dbcard.CardRepository;
import com.zarubovandlevchenko.lb1.repository.dbuser.UserRepository;
import com.zarubovandlevchenko.lb1.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration-requests")
public class SecurityAdminController {
    private final SecurityAdminService securityAdminService;
    private final NewUsersStorageService newUsersStorageService;
    private final UserService userService;



    @Autowired
    private UserRepository userRepository;

    @PersistenceContext(unitName = "usersDbEntityManagerFactory")
    private EntityManager entityManager;
    @Autowired
    private CardRepository cardRepository;

    @GetMapping
    public Map<Long, UserModal> getRequests() {
        return newUsersStorageService.getRegistrationRequests();
    }

    @PatchMapping("/{requestId}")
    public String updateStatus(@PathVariable Long requestId, @RequestBody StatusUpdateRequest request) throws Exception {
        return securityAdminService.updateRequestStatus(requestId, request.getStatus());
    }

}
