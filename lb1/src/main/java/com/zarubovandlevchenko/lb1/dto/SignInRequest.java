package com.zarubovandlevchenko.lb1.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInRequest {

    private String login;
    private String name;
    private String password;
    private String card_number;
    private String phoneNumber;
}
