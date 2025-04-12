package com.zarubovandlevchenko.lb1.controller;
import com.zarubovandlevchenko.lb1.dto.SignUpRequest;
import com.zarubovandlevchenko.lb1.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @GetMapping("/form")
    public String getRegisterForm() {
        return registerService.getRegisterForm();
    }

    @PostMapping("/no-card")
    public String dontHaveCard() {
        return registerService.getRegisterNotify();
    }

    @PostMapping
    public ResponseEntity<?> registration(@RequestBody SignUpRequest request) {
        return registerService.registrate(request);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        return registerService.verifyOtp(phoneNumber, otp);
    }

    @PatchMapping("/otp/new")
    public ResponseEntity<?> newOtp(@RequestParam String phoneNumber) {
        return registerService.sendNewOtp(phoneNumber);
    }
}
