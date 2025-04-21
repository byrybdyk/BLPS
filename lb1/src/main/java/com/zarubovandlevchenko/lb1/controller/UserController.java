package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserModal> getAllUsers() {
        return userService.getUsers();
    }
}
