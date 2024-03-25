package com.ky.productservice.exc;

public class CategoryNameNotProvidedException extends RuntimeException {
    public CategoryNameNotProvidedException(String message) {
        super(message);
    }
}