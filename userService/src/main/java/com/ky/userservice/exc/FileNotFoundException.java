package com.ky.userservice.exc;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class FileNotFoundException extends RuntimeException{
    public FileNotFoundException(String message){
        super(message);
    }
}
