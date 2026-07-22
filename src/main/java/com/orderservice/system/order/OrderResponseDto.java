package com.orderservice.system.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private String orderId;
    private String paymentStatus;;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private List<OrderItemsResponseDto> orderItems;
    private BigDecimal totalPrice;
    private String deliveryStatus;
    private String cartId;
}
