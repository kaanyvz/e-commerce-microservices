package com.ky.productservice.request.update;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateProductRequest {
    private String newProductName;
    private String desc;
    private Long categoryId;
    private BigDecimal price;
    private String imageUrl;
}
