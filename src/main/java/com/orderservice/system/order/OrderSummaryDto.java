package com.orderservice.system.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSummaryDto {
    private long totalReceipts;
    private long paidReceipts;
    private long pendingReceipts;
    private BigDecimal totalSales;

    private List<OrderEntity> paidOrders;    // ✅ add this
    private List<OrderEntity> pendingOrders; // ✅ add this
}

