package com.ky.orderservice.mapper;

import com.ky.orderservice.dto.OrderDto;
import com.ky.orderservice.model.Order;
import com.ky.orderservice.model.OrderStatus;
import com.ky.orderservice.request.CreateOrderRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;
    private final OrderAddressMapper orderAddressMapper;

    public OrderMapper(OrderItemMapper orderItemMapper, OrderAddressMapper orderAddressMapper) {
        this.orderItemMapper = orderItemMapper;
        this.orderAddressMapper = orderAddressMapper;
    }

    public OrderDto orderConverter(Order order){
        return OrderDto.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .addressDto(orderAddressMapper.orderAddressToOrderAddressDto(order.getOrderAddress()))
                .customerId(order.getCustomerId())
                .localDateTime(order.getCreatedDate())
                .items(order.getOrderItems()
                        .stream()
                        .map(orderItemMapper::orderItemToOrderItemDto)
                        .collect(Collectors.toList()))
                .build();

    }

    public Order orderReqToOrder(CreateOrderRequest orderRequest){
        return Order.builder()
                .customerId((Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .orderAddress(orderAddressMapper.orderAddressRequestToOrderAddress(orderRequest.getAddress()))
                .orderStatus(OrderStatus.PENDING)
                .orderItems(orderRequest.getItems()
                        .stream().map(orderItemMapper::orderItemRequestToOrderItem)
                        .collect(Collectors.toList()))
                .build();
    }
}
