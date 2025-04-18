package com.zarubovandlevchenko.lb1.controller;


import com.zarubovandlevchenko.lb1.dto.SignInRequest;
import com.zarubovandlevchenko.lb1.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignInRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String cardNumber, @RequestParam String otp) {
        return authService.verifyOtp(cardNumber, otp);
    }
    @PostMapping("/otp/new")
    public ResponseEntity<?> newOtp(@RequestParam String cardNumber) {
        return authService.sendNewOtp(cardNumber);
    }

}

