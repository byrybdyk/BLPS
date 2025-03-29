package com.zarubovandlevchenko.lb1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPinException extends RuntimeException {
    public InvalidPinException() {
        super("Pin must be a 4-digit positive number");
    }
}
