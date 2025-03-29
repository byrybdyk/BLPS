package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.dto.SignInRequest;
import com.zarubovandlevchenko.lb1.model.Card;
import com.zarubovandlevchenko.lb1.model.UserModal;
import com.zarubovandlevchenko.lb1.repository.UserRepository;
import com.zarubovandlevchenko.lb1.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final OtpStorageService otpStorageService;


    public ResponseEntity<?> authenticate(SignInRequest request) {
        if (request.getCard_number() != null) {
            return handleCardAuthentication(request.getCard_number());
        } else {
            return handlePasswordAuthentication(request.getLogin(), request.getPassword());
        }
    }

    private ResponseEntity<?> handleCardAuthentication(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber);
        if (card == null) {
            return ResponseEntity.badRequest().body("Карта не найдена");
        }

        otpStorageService.sendOtp(cardNumber);

        return ResponseEntity.ok("OTP отправлен");
    }

    private ResponseEntity<?> handlePasswordAuthentication(String login, String password) {
        UserModal user = userRepository.findByLoginOrPhoneNumber(login, login);
        if (user == null) {
            return ResponseEntity.badRequest().body("Неверный логин или пароль");
        }

        return ResponseEntity.ok(Map.of("message", "Вход успешный", "user", user.getLogin()));
    }

    public ResponseEntity<?> verifyOtp(String cardNumber, String otp) {
        Map<String, String> otps = otpStorageService.getAllOtps();
        if (!otps.containsKey(cardNumber) || !otps.get(cardNumber).equals(otp)) {
            return ResponseEntity.badRequest().body("Неверный OTP");
        }

        otpStorageService.removeOtp(cardNumber);
        Card card = cardRepository.findByCardNumber(cardNumber);
        return ResponseEntity.ok(Map.of("message", "Вход успешный"));
    }

    public ResponseEntity<?> sendNewOtp(String cardNumber) {
        System.out.println(otpStorageService.getAllOtps());
        if (!otpStorageService.getAllOtps().containsKey(cardNumber)) {
            System.out.println("OTP не отправлялся на номер привязанный к карте " + cardNumber);
            return ResponseEntity.badRequest().body("OTP не отправлялся на этот номер");
        }
        otpStorageService.sendNewOtp(cardNumber);
        return ResponseEntity.ok("Новый OTP отправлен");
    }

}
