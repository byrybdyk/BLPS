package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.dto.SignInRequest;
import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.repository.UserRepository;
import com.zarubovandlevchenko.lb1.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final OtpStorageService otpStorageService;


    public List<?> authenticate(SignInRequest request) {
        return handlePasswordAuthentication(request.getLogin(), request.getPassword(),request.getPhoneNumber());
    }

    private ResponseEntity<?> handleCardAuthentication(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Card with number " + cardNumber + "didn't found");
        }

        otpStorageService.sendOtp(cardNumber);

        return ResponseEntity.ok("OTP отправлен");
    }

    private List<?> handlePasswordAuthentication(String login, String password,String phoneNumber) {
        UserModal user = userRepository.findByLoginOrPhoneNumber(login, phoneNumber);
        if (user == null) {
            return null;
        }
        if (!user.getPassword().equals(password)) {
            return null;
        }
        return cardRepository.findAllByUser(user.getId());
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
