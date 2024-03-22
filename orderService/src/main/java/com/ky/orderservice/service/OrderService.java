package com.ky.orderservice.service;

import com.ky.orderservice.client.ProductServiceClient;
import com.ky.orderservice.dto.OrderDto;
import com.ky.orderservice.dto.Pagination;
import com.ky.orderservice.mapper.OrderMapper;
import com.ky.orderservice.model.Order;
import com.ky.orderservice.repository.OrderRepository;
import com.ky.orderservice.request.CreateOrderItemRequest;
import com.ky.orderservice.request.CreateOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductServiceClient productServiceClient;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ProductServiceClient productServiceClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productServiceClient = productServiceClient;
    }

    //todo - continue to this func. - 03.19
    public OrderDto createOrder(CreateOrderRequest request){
        Order order = orderMapper.orderReqToOrder(request);
        order.getOrderAddress().setOrder(order);
        order.getOrderItems()
                .forEach(orderItem -> orderItem.setOrder(order));
        for(CreateOrderItemRequest itemRequest : request.getItems()){
            ResponseEntity<Boolean> isInStock = productServiceClient.isInStock(itemRequest.getProductId());
            if(!isInStock.getBody()){
                throw new RuntimeException("Product with ID: " + itemRequest.getProductId() + " is not in stock");
            }else{
                productServiceClient.reduceStock(itemRequest.getProductId(), itemRequest.getQty());
            }
        }

        return orderMapper.orderConverter(orderRepository.save(order));
    }

    public Pagination<OrderDto> getAllOrders(int pageNo, int pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Order> orders = orderRepository.findAll(paging);
        return new Pagination<>(orders.stream().map(orderMapper::orderConverter).collect(Collectors.toList()),
                (int) orders.getTotalElements());
    }
}
