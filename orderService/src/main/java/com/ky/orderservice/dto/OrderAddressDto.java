package com.ky.orderservice.dto;

public record OrderAddressDto(
        String city,
        String district,
        String addressDetail
) {
}
