package com.orderservice.system.order;

import com.orderservice.system.checkout.CheckoutResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();



    public static String generateOrderCode() {
            StringBuilder sb = new StringBuilder(LENGTH);
            for (int i = 0; i < LENGTH; i++) {
                int index = RANDOM.nextInt(CHAR_POOL.length());
                sb.append(CHAR_POOL.charAt(index));
            }
            return sb.toString();
        }

    public CheckoutResponseDto markOrderAsPaid(Long orderId) {
        var order = orderRepository.findByOrderId(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);

        return new CheckoutResponseDto(
                order.getOrderId(),
                order.getCustomerId(),
                order.getPaymentStatus().name()
        );
    }
    public List<OrderResponseDto> getAllOrders() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return orderRepository.findTop7ByOrderDateBetweenAndPaymentStatus(
                        startOfDay, endOfDay, "PENDING"
                )
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

//    public List<OrderResponseDto> getAllOrders() {
//        return orderRepository.findAll()
//                .stream()
//                .map(orderMapper::toDto)
//                .toList();
//    }

    public OrderResponseDto getOrderById(Long orderId) {
        var order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException());
        return orderMapper.toDto(order);
    }

    public OrderSummaryDto getOrderSummary() {
        List<OrderResponseDto> orders = getAllOrders();

        long totalReceipts = orders.size();
        long paidReceipts = orders.stream()
                .filter(o -> "Paid".equalsIgnoreCase(o.getPaymentStatus()))
                .count();
        long pendingReceipts = orders.stream()
                .filter(o -> "Pending".equalsIgnoreCase(o.getPaymentStatus()))
                .count();
        BigDecimal totalSales = orders.stream()
                .filter(o -> "Paid".equalsIgnoreCase(o.getPaymentStatus()))
                .map(OrderResponseDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderSummaryDto summary = new OrderSummaryDto();
        summary.setTotalReceipts(totalReceipts);
        summary.setPaidReceipts(paidReceipts);
        summary.setPendingReceipts(pendingReceipts);
        summary.setTotalSales(totalSales);
        return summary;
    }


}
