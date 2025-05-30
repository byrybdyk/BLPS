package com.zarubovandlevchenko.lb1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidLimitException extends RuntimeException {
    public InvalidLimitException() {
        super("Limit must be greater than or equal to 0");
    }
}