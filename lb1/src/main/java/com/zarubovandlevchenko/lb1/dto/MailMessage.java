package com.zarubovandlevchenko.lb1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailMessage {
    private String to;
    private String subject;
    private String body;
}
