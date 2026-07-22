package com.orderservice.system.cart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartResponseDto {
    private String id;
    private List<CartItemsDto> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
