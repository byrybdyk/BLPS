package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.dto.SignUpRequest;
import com.zarubovandlevchenko.lb1.jira.JiraConnectionImpl;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import lombok.RequiredArgsConstructor;
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

    public ResponseEntity<?> verifyOtp(String phoneNumber, String otp) {
        otpStorageService.getAllOtps();
        if (!otpStorageService.getAllOtps().containsKey(phoneNumber) || !otpStorageService.getAllOtps().get(phoneNumber).equals(otp)) {
            return ResponseEntity.badRequest().body("Неверный OTP");
        }

        otpStorageService.removeOtp(phoneNumber);
        UserModal newUser = newUsersStorageService.getNewUser(phoneNumber);
        if (newUser != null) {
            newUsersStorageService.removeNewUser(newUser);
            newUsersStorageService.addUsersRegistrationRequest(newUser);

            try {
                jiraConnection.createIssue(newUser);
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
