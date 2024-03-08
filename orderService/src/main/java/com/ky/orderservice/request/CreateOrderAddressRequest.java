package com.ky.orderservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateOrderAddressRequest {
    private String city;
    private String district;
    private String addressDetail;
}
