package com.ky.productservice.request.create;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateProductRequest {
    private String name;
    private Integer categoryId;
    private BigDecimal price;
    private Integer stockCount;
    private String desc;
}
