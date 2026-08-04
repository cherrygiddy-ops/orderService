package com.orderservice.system.order;

import com.orderservice.system.checkout.CheckoutResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;
    // List all orders
    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get specific order by ID
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/{orderId}/status/paid")
    public CheckoutResponseDto markOrderAsPaid(@PathVariable Long orderId) {
        return orderService.markOrderAsPaid(orderId);
    }

    @GetMapping("/summary")
    public OrderSummaryDto getOrderSummary() {
        return orderService.getOrderSummary();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?>handleOrderNotFound(){
        return ResponseEntity.notFound().build();
    }


}
