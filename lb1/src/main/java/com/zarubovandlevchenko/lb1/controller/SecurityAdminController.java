package com.zarubovandlevchenko.lb1.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zarubovandlevchenko.lb1.dto.StatusUpdateRequest;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.repository.CardRepository;
import com.zarubovandlevchenko.lb1.repository.UserRepository;
import com.zarubovandlevchenko.lb1.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration-requests")
public class SecurityAdminController {
    private final SecurityAdminService securityAdminService;
    private final NewUsersStorageService newUsersStorageService;
    private final UserService userService;



//    @Autowired
//    private UserRepository userRepository;
//
//    @PersistenceContext(unitName = "usersDbEntityManagerFactory")
//    private EntityManager entityManager;
//    @Autowired
//    private CardRepository cardRepository;

    @GetMapping
    public Map<Long, UserModal> getRequests() {
        return newUsersStorageService.getRegistrationRequests();
    }

    @PatchMapping("/{requestId}")
    public String updateStatus(@PathVariable Long requestId, @RequestBody StatusUpdateRequest request) throws Exception {
        return securityAdminService.updateRequestStatus(requestId, request.getStatus());
    }

    @PostMapping("/webhook/issue/status")
    public String rejectRequest( @RequestBody String body) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(body);

        String summary = root.path("issue").path("fields").path("summary").asText();
        String status = root.path("issue").path("fields").path("status").path("name").asText().toUpperCase();
        System.out.println("Received webhook for issue with summary: " + summary + " and status: " + status);
        return securityAdminService.updateRequestStatus(Long.valueOf(summary), status);
    }

}
