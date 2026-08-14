package com.orderservice.system.order;

import com.orderservice.system.checkout.CheckoutResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final ZoneId NAIROBI_ZONE = ZoneId.of("Africa/Nairobi");



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
        ZoneId nairobiZone = ZoneId.of("Africa/Nairobi");
        LocalDate today = LocalDate.now(nairobiZone);

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
                .orElseThrow(OrderNotFoundException::new);

        OrderResponseDto dto = orderMapper.toDto(order);

        // Interpret stored LocalDateTime as UTC, then convert to Nairobi
        ZonedDateTime nairobiTime = order.getOrderDate()
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Africa/Nairobi"));

        // DTO expects LocalDateTime, so set directly
        dto.setOrderDate(nairobiTime.toLocalDateTime());

        return dto;
    }



    public OrderSummaryDto getOrderSummary() {

        Date start = getStartOfToday();
        Date end = getEndOfToday();

        long paid = orderRepository
                .countByPaymentStatusAndOrderDateBetween("PAID", start, end);

        long pending = orderRepository
                .countByPaymentStatusAndOrderDateBetween("PENDING", start, end);

        List<OrderEntity> paidOrders = orderRepository
                .findByPaymentStatusAndOrderDateBetween("PAID", start, end);

        BigDecimal sales = paidOrders.stream()
                .map(OrderEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<OrderEntity> pendingOrders = orderRepository
                .findByPaymentStatusAndOrderDateBetween("PENDING", start, end);

        OrderSummaryDto summary = new OrderSummaryDto();
        summary.setTotalReceipts(paid);
        summary.setPaidReceipts(paid);
        summary.setPendingReceipts(pending);
        summary.setTotalSales(sales);

        return summary;
    }

    private Date getStartOfToday() {
        return Date.from(
                LocalDate.now(NAIROBI_ZONE)
                        .atStartOfDay(NAIROBI_ZONE)
                        .toInstant()
        );
    }

    private Date getEndOfToday() {
        return Date.from(
                LocalDate.now(NAIROBI_ZONE)
                        .plusDays(1)
                        .atStartOfDay(NAIROBI_ZONE)
                        .toInstant()
        );
    }




}
