package com.ky.productservice.exc;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserNotAuthenticatedException extends RuntimeException{
    public UserNotAuthenticatedException(String message){
        super(message);
    }
}
