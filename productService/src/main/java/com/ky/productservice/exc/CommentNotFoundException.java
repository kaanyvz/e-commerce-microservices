package com.ky.productservice.exc;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String message) {
        super(message);
    }
}
