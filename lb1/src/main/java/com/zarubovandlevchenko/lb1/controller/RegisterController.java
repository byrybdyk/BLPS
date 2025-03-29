package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.dto.SignInRequest;
import com.zarubovandlevchenko.lb1.dto.SignUpRequest;
import com.zarubovandlevchenko.lb1.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @GetMapping("/registerForm")
    public ResponseEntity<?> getRegisterForm() {
        String message = registerService.getRegisterForm();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/dontHaveCard")
    public ResponseEntity<?> dontHaveCard() {
        String message = registerService.getRegisterNotify();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody SignUpRequest request) {
        return registerService.registrate(request);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        return registerService.verifyOtp(phoneNumber, otp);
    }

    @PatchMapping("/new-otp")
    public ResponseEntity<?> newOtp(@RequestParam String phoneNumber) {
        return registerService.sendNewOtp(phoneNumber);
    }

}
