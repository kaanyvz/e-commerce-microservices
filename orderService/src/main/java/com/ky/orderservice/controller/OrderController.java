package com.ky.orderservice.controller;

import com.ky.orderservice.dto.OrderDto;
import com.ky.orderservice.dto.Pagination;
import com.ky.orderservice.request.CreateOrderRequest;
import com.ky.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping("/createOrder")
    public ResponseEntity<OrderDto> createNewOrder(@RequestBody CreateOrderRequest request){
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<Pagination<OrderDto>> getAll(@RequestParam(required = false,defaultValue = "0")  int pageNo,
                                                       @RequestParam(required = false,defaultValue = "10") int pageSize){
        return ResponseEntity.ok(orderService.getAllOrders(pageNo,pageSize));
    }
}
