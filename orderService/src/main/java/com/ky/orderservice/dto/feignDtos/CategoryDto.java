package com.ky.orderservice.dto.feignDtos;

import lombok.Builder;

@Builder
public record CategoryDto (
        Integer id,
        String name
) {
}
