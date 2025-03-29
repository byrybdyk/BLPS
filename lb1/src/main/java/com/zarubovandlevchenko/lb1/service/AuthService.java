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

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

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

        String otp = generateOtp();
        otpStorage.put(cardNumber, otp);
        sendOtp(card.getUser().getPhoneNumber(), otp);

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
        if (!otpStorage.containsKey(cardNumber) || !otpStorage.get(cardNumber).equals(otp)) {
            return ResponseEntity.badRequest().body("Неверный OTP");
        }

        otpStorage.remove(cardNumber);
        Card card = cardRepository.findByCardNumber(cardNumber);
        return ResponseEntity.ok(Map.of("message", "Вход успешный"));
    }

    private String generateOtp() {
        return "12345";
    }

    private void sendOtp(String phoneNumber, String otp) {
        System.out.println("Отправка OTP " + otp + " на номер " + phoneNumber);
    }
}
