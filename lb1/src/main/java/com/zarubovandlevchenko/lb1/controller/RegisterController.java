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
    public ResponseEntity<?> getRegisterForm() {
        String message = registerService.getRegisterForm();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/no-card")
    public ResponseEntity<?> dontHaveCard() {
        String message = registerService.getRegisterNotify();
        return ResponseEntity.ok(message);
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
