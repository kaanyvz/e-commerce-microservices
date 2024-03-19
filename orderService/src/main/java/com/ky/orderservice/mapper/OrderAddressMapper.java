package com.ky.orderservice.mapper;

import com.ky.orderservice.dto.OrderAddressDto;
import com.ky.orderservice.model.OrderAddress;
import com.ky.orderservice.request.CreateOrderAddressRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderAddressMapper {
    public OrderAddressDto orderAddressToOrderAddressDto(OrderAddress orderAddress){
        return OrderAddressDto.builder()
                .addressDetail(orderAddress.getAddressDetail())
                .city(orderAddress.getCity())
                .district(orderAddress.getDistrict())
                .build();
    }

    public OrderAddress orderAddressRequestToOrderAddress(CreateOrderAddressRequest createOrderAddressRequest){
        return OrderAddress.builder()
                .city(createOrderAddressRequest.getCity())
                .district(createOrderAddressRequest.getDistrict())
                .addressDetail(createOrderAddressRequest.getAddressDetail())
                .build();
    }
}