package com.ky.orderservice.dto;

public record OrderItemDto(
        Integer productId,
        Integer qty
) {
}
