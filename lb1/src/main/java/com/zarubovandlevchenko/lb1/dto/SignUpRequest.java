package com.zarubovandlevchenko.lb1.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRequest {
    String name;
    String lastName;
    String phoneNumber;
    String passport;
}
