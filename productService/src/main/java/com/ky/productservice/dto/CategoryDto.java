package com.ky.productservice.dto;

import lombok.Builder;

@Builder
public record CategoryDto (
        Integer id,
        String name
) {
}
