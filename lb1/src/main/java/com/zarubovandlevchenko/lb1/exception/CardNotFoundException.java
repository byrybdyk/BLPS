package com.zarubovandlevchenko.lb1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long id) {
        super("Card with ID " + id + " not found");
    }
}
