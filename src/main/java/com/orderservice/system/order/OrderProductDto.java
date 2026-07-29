package com.orderservice.system.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private String id;
    private String name;
    private BigDecimal price;
}
