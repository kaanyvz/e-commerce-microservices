package com.ky.orderservice.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {
    private CreateOrderAddressRequest address;
    private List<CreateOrderItemRequest> items;
}
