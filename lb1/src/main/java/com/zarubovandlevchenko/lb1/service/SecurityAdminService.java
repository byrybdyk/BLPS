package com.zarubovandlevchenko.lb1.service;
import com.atomikos.jdbc.internal.AtomikosSQLException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zarubovandlevchenko.lb1.Exception.InvalidStatusException;
import com.zarubovandlevchenko.lb1.exception.UserNotFoundException;
import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.repository.dbuser.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.zarubovandlevchenko.lb1.xml.UserRegistration;
import org.springframework.transaction.TransactionStatus;

import java.sql.SQLException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SecurityAdminService {
    private final NewUsersStorageService newUsersStorageService;
    private final UserService userService;
    private final CardService cardService;
    private final UserRepository userRepository;
    private final TransactionHelper transactionHelper;
    private final JiraService jiraService;

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

    public void approveRequest(Long requestId) throws Exception {
        TransactionStatus status = transactionHelper.createTransaction("approveRequest");
        UserRegistration userRegistration = new UserRegistration();
        String userId = null;
        UserModal userModal = null;
        boolean registeredInXml = false;
        boolean requestRemoved = false;
        Card card = null;
        try {
            if (newUsersStorageService.getRegistrationRequests().containsKey(requestId)) {
                System.out.println("Processing registration request with ID: " + requestId);
                userModal = userService.saveUser(newUsersStorageService.getRegistrationRequests().get(requestId));
                userId = userModal.getId().toString();
                System.out.println("User saved with ID: " + userId);
                card = cardService.createCard(userModal);

                System.out.println("Card created with number: " + card.getCardNumber());
                System.out.println("Карта готова, можете идти забирать");
            }

            userRegistration.registerUser(
                    userId,
                    "USER" + Objects.requireNonNull(userModal).getId(),
                    "USER" + userModal.getId(),
                    card.getCardNumber(),
                    userModal.getPassportNumber());
            registeredInXml = true;
            System.out.println("User registered in XML");
            newUsersStorageService.removeUsersRegistrationRequestById(requestId);
            requestRemoved = true;
            System.out.println("Registration request removed");
//            if(true){
//                throw new RuntimeException("before commit");
//            }
            transactionHelper.commit(status);
            System.out.println("Transaction committed successfully");
        } catch (Exception e) {
            System.out.println("Error processing request ID: " + requestId + ". Rolling back transaction: " + e.getMessage());
            transactionHelper.rollback(status);
            if (registeredInXml) {
                try {
                    userRegistration.unregisterUser(userId);
                    System.out.println("User " + userId + " unregistered from XML");
                } catch (Exception ex) {
                    System.out.println("Failed to unregister user " + userId + " from XML: " + ex.getMessage());
                }
            }
            if (requestRemoved) {
                try {
                    newUsersStorageService.restoreUserRegistrationRequest(userModal);
                    System.out.println("Restored registration request for user: " + userModal.getPhoneNumber());

                } catch (Exception ex) {
                    System.out.println("Failed to restore registration request for user: " + userModal.getPhoneNumber() + ": " + ex.getMessage());
                }
            }
            throw e;
        }
    }

    public void rejectRequest(Long requestId) {
        if (newUsersStorageService.getRegistrationRequests().containsKey(requestId)) {
            newUsersStorageService.removeUsersRegistrationRequestById(requestId);
            System.out.println("Ваша заявка отклонена");
        }
    }

    public void approveRequestByPhone(String phoneNumber) throws Exception {
        UserModal user = findUserByPhone(phoneNumber);
        if (user != null) {
            Long requestId = (long) user.hashCode();
            approveRequest(requestId);
        }
    }

    public void rejectRequestByPhone(String phoneNumber) {
        UserModal user = findUserByPhone(phoneNumber);
        if (user != null) {
            Long requestId = (long) user.hashCode();
            rejectRequest(requestId);
        }
    }

    private UserModal findUserByPhone(String phoneNumber) {
        for (UserModal user : newUsersStorageService.getRegistrationRequests().values()) {
            if (phoneNumber.equals(user.getPhoneNumber())) {
                return user;
            }
        }
        return null;
    }
}
