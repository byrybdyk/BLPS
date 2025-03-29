package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.model.UserModal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityAdminService {
    private final NewUsersStorageService newUsersStorageService;
    private final UserService userService;
    private final CardService cardService;

    public String approveRequest(Long requestId) {
        if (newUsersStorageService.getRegistrationRequests().containsKey(requestId)) {
            UserModal user = userService.saveUser(newUsersStorageService.getRegistrationRequests().get(requestId));
            cardService.createCard(user);
            newUsersStorageService.removeUsersRegistrationRequestById(requestId);
            System.out.println("Карта готова, можете идти забирать");
            return "Request approved";
        } else {
            return "Request not found";
        }
    }

    public String rejectRequest(Long requestId) {
        if (newUsersStorageService.getRegistrationRequests().containsKey(requestId)) {
            newUsersStorageService.removeUsersRegistrationRequestById(requestId);
            System.out.println("Ваша заявка отклонена");
            return "Request rejected";
        } else {
            return "Request not found";
        }
    }
}
