package com.ky.productservice.exc;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String message) {
        super(message);
    }
}
