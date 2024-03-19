package com.ky.orderservice.dto;

import lombok.Builder;

@Builder
public record OrderItemDto(
        Integer productId,
        Integer qty
) {
}
