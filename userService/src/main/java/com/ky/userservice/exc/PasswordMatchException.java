package com.ky.userservice.exc;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PasswordMatchException extends RuntimeException{
    public PasswordMatchException(String message){
        super(message);
    }
}