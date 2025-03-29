package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.model.UserModal;
import com.zarubovandlevchenko.lb1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}/goToBank")
    public ResponseEntity<Void> goToBank(@PathVariable String id) {
        System.out.println("Клиент " + id + " пришёл в банк.");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserModal>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }
}
