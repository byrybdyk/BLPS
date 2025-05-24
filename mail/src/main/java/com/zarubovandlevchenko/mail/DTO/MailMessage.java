package com.zarubovandlevchenko.mail.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailMessage {
    private String to;
    private String subject;
    private String body;

}

