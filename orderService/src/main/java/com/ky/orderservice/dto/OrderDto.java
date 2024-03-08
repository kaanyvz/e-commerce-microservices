package com.ky.orderservice.dto;

import com.ky.orderservice.model.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDto(
        Integer id,
        Integer customerId,
        OrderAddressDto addressDto,
        List<OrderItemDto> items,
        OrderStatus orderStatus,
        LocalDateTime localDateTime
) {
}
