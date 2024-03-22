package com.ky.orderservice.model;

import com.ky.commons.model.AdvanceBaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderMethodName = "orderBuilder")
public class Order extends AdvanceBaseModel {
    private String customerMail;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderAddress orderAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;
}
