package com.orderservice.system.order;

import lombok.Data;

@Data
public class AddOrderItemRequest {
    private String productId;
    private int quantity;
}

