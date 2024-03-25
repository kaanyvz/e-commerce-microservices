package com.ky.productservice.exc;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(String message){
        super(message);
    }
}
