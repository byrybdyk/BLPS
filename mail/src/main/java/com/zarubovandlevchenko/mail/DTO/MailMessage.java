package com.zarubovandlevchenko.mail.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessage {
    private String to;
    private String subject;
    private String body;

}

