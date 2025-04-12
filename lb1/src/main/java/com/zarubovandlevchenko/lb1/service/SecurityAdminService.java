package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.Exception.InvalidStatusException;
import com.zarubovandlevchenko.lb1.model.UserModal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityAdminService {
    private final NewUsersStorageService newUsersStorageService;
    private final UserService userService;
    private final CardService cardService;

    public String updateRequestStatus(Long requestId, String status) {
        if (newUsersStorageService.getRegistrationRequests().containsKey(requestId)) {
            if ("APPROVED".equals(status)) {
                approveRequest(requestId);
                return "Approved";
            } else if ("REJECTED".equals(status)) {
                rejectRequest(requestId);
                return "Rejected";
            } else {
                throw new InvalidStatusException("Invalid status" + status);
            }
        }
        else{
            return "Request not found";
        }
    }

    public void approveRequest(Long requestId) {
        if (newUsersStorageService.getRegistrationRequests().containsKey(requestId)) {
            UserModal user = userService.saveUser(newUsersStorageService.getRegistrationRequests().get(requestId));
            cardService.createCard(user);
            newUsersStorageService.removeUsersRegistrationRequestById(requestId);
            System.out.println("Карта готова, можете идти забирать");
        }
    }

    public void rejectRequest(Long requestId) {
        if (newUsersStorageService.getRegistrationRequests().containsKey(requestId)) {
            newUsersStorageService.removeUsersRegistrationRequestById(requestId);
            System.out.println("Ваша заявка отклонена");
        }
    }
}
