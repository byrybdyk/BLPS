package com.zarubovandlevchenko.lb1.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpStorageService {
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public void saveOtp(String phoneNumber, String otp) {
        otpStorage.put(phoneNumber, otp);
        System.out.println("OTP " + otp + " Отправлено на номер " + phoneNumber);
    }

    public String getOtp(String phoneNumber) {
        return otpStorage.get(phoneNumber);
    }

    public void sendOtp(String phoneNumber) {
        String otp = generateOtp();
        saveOtp(phoneNumber, otp);
    }

    public void removeOtp(String phoneNumber) {
        otpStorage.remove(phoneNumber);
    }

    public void sendNewOtp(String phoneNumber) {
        removeOtp(phoneNumber);
        sendOtp(phoneNumber);
    }

    public Map<String, String> getAllOtps() {
        return otpStorage;
    }
    private String generateOtp() {
        return "12345";
    }
}