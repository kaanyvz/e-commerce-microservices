package com.ky.orderservice.dto;

import lombok.Builder;

@Builder
public record OrderAddressDto(
        String city,
        String district,
        String addressDetail
) {
}
