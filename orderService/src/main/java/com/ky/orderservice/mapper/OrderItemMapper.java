package com.ky.orderservice.mapper;

import com.ky.orderservice.dto.OrderItemDto;
import com.ky.orderservice.model.OrderItem;
import com.ky.orderservice.request.CreateOrderItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItemDto orderItemToOrderItemDto(OrderItem orderItem){
        return OrderItemDto.builder()
                .qty(orderItem.getQty())
                .productId(orderItem.getProductId())
                .build();
    }

    public OrderItem orderItemRequestToOrderItem(CreateOrderItemRequest createOrderItemRequest){
        return OrderItem.builder()
                .productId(createOrderItemRequest.getProductId())
                .qty(createOrderItemRequest.getQty())
                .build();
    }
}
