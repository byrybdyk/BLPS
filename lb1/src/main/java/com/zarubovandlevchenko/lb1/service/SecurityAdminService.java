package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.Exception.InvalidStatusException;
import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.repository.dbuser.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.zarubovandlevchenko.lb1.xml.UserRegistration;
@Service
@RequiredArgsConstructor
public class SecurityAdminService {
    private final NewUsersStorageService newUsersStorageService;
    private final UserService userService;
    private final CardService cardService;
    private final UserRepository userRepository;

    public String updateRequestStatus(Long requestId, String status) throws Exception {
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

    @Transactional
    public void approveRequest(Long requestId) throws Exception {



        if (newUsersStorageService.getRegistrationRequests().containsKey(requestId)) {
            UserModal user = userService.saveUser(newUsersStorageService.getRegistrationRequests().get(requestId));
            UserModal currentUser = userRepository.findUserModalByPassportNumber(user.getPassportNumber())
                    .orElseThrow(() -> new IllegalStateException("User with passport number " + user.getPassportNumber() + " not found after saving"));
            Card card = cardService.createCard(currentUser);
            UserRegistration userRegistration = new UserRegistration();
            userRegistration.registerUser(currentUser.getId().toString(), "USER" + currentUser.getId().toString(), "USER" + currentUser.getId().toString(),card.getCardNumber(), currentUser.getPassportNumber());
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
