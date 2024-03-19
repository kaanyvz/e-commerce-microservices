package com.ky.orderservice.service;

import com.ky.orderservice.dto.OrderDto;
import com.ky.orderservice.mapper.OrderMapper;
import com.ky.orderservice.model.Order;
import com.ky.orderservice.repository.OrderRepository;
import com.ky.orderservice.request.CreateOrderRequest;
import org.springframework.stereotype.Service;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    //todo - continue to this func. - 03.19
    public OrderDto createOrder(CreateOrderRequest request){
        Order order = orderMapper.orderReqToOrder(request);
        order.getOrderAddress().setOrder(order);
        order.getOrderItems()
                .forEach(orderItem -> orderItem.setOrder(order));

        return null;
    }
}
