package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.dto.SignUpRequest;
import com.zarubovandlevchenko.lb1.jira.JiraConnectionImpl;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserService userService;
    private final OtpStorageService otpStorageService;
    private final NewUsersStorageService newUsersStorageService;
    private final JiraConnectionImpl jiraConnection;


    public String getRegisterForm() {
        String registerForm = """
                {
                    "firstName": "",
                    "lastName": "",
                    "phoneNumber": "",
                    "passportNumber": ""
                }
                """;
        return registerForm;
    }

    public String getRegisterNotify() {
        String notify = """
                {
                    notify: "You must register a card",
                }
                """;
        return notify;
    }

    public ResponseEntity<String> registrate(SignUpRequest request) {
        UserModal newUser = userService.validateAndCreateUser(request);
        newUser.setLogin(request.getPhoneNumber());
        newUser.setPassword("USER");
        newUsersStorageService.addNewUser(newUser, request.getPhoneNumber());
        return handlePhonedAuthentication(request.getPhoneNumber());
    }

    private ResponseEntity<String> handlePhonedAuthentication(String phoneNumber) {
        otpStorageService.sendOtp(phoneNumber);
        return ResponseEntity.ok("OTP отправлен");
    }

    public Boolean verifyOTPS(String phoneNumber, String otp) {
        otpStorageService.getAllOtps();
        System.out.println("Проверка OTP для номера: " + phoneNumber);
        System.out.println("OTP: " + otp);
        Boolean result = (!otpStorageService.getAllOtps().containsKey(phoneNumber) || !otpStorageService.getAllOtps().get(phoneNumber).equals(otp));
        if(!result){
            otpStorageService.removeOtp(phoneNumber);
        }
        return result;
    }

    public void sendTaskToJira(String phoneNumber){
        UserModal newUser = newUsersStorageService.getNewUser(phoneNumber);
        if (newUser != null) {
            newUsersStorageService.removeNewUser(newUser);
            Long requestId = newUsersStorageService.addUsersRegistrationRequest(newUser);

            try {
                jiraConnection.createIssue(newUser,requestId);
            } catch (Exception e) {
                System.out.println(("Ошибка при создании заявки в JIRA"+ e));
            }
        } else {
        }
    }

    public ResponseEntity<?> verifyOtp(String phoneNumber, String otp) {
        otpStorageService.getAllOtps();
        System.out.println("Проверка OTP для номера: " + phoneNumber);
        System.out.println("OTP: " + otp);
        if (!otpStorageService.getAllOtps().containsKey(phoneNumber) || !otpStorageService.getAllOtps().get(phoneNumber).equals(otp)) {
            System.out.println("OTP не совпадает или не найден для номера: " + phoneNumber);
            throw new BpmnError("otpError");
        }

        otpStorageService.removeOtp(phoneNumber);
        UserModal newUser = newUsersStorageService.getNewUser(phoneNumber);
        if (newUser != null) {
            newUsersStorageService.removeNewUser(newUser);
            Long requestId = newUsersStorageService.addUsersRegistrationRequest(newUser);

            try {
                jiraConnection.createIssue(newUser,requestId);
            } catch (Exception e) {
                System.out.println(("Ошибка при создании заявки в JIRA"+ e));
            }
            return ResponseEntity.ok("Ожидайте подтверждения регистрации");
        } else {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
    }

    public ResponseEntity<?> sendNewOtp(String phoneNumber) {
        System.out.println(otpStorageService.getAllOtps());
        if (!otpStorageService.getAllOtps().containsKey(phoneNumber)) {
            System.out.println("OTP не отправлялся на номер " + phoneNumber);
            return ResponseEntity.badRequest().body("OTP не отправлялся на этот номер");
        }
        otpStorageService.sendNewOtp(phoneNumber);
        return ResponseEntity.ok("Новый OTP отправлен");
    }
}
