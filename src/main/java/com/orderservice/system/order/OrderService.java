package com.orderservice.system.order;

import com.orderservice.system.checkout.CheckoutResponseDto;
import com.orderservice.system.products.ProductsEntity;
import com.orderservice.system.products.ProductsRepository;
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
    private final ProductsRepository productsRepository;

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


    public OrderResponseDto addItemsToOrder(Long orderId, List<AddOrderItemRequest> items) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        for (AddOrderItemRequest req : items) {
            ProductsEntity product = productsRepository.findById(req.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            // Check if item already exists
            OrderItemsEntity existingItem = order.getOrderItems().stream()
                    .filter(i -> i.getProduct().getId().equals(product.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                // Update quantity and total
                existingItem.setQuantity(existingItem.getQuantity() + req.getQuantity());
                existingItem.setTotalPrice(
                        product.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity()))
                );
            } else {
                // Add new item if not found
                OrderItemsEntity newItem = new OrderItemsEntity(product, req.getQuantity());
                order.addItems(newItem);
            }
        }

        // Recalculate total
        BigDecimal total = order.getOrderItems().stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);

        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    public OrderResponseDto removeItemFromOrder(Long orderId, String productId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        // Find the item to remove
        OrderItemsEntity itemToRemove = order.getOrderItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in order"));

        // Remove it
        order.getOrderItems().remove(itemToRemove);

        // Recalculate total
        BigDecimal total = order.getOrderItems().stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);

        orderRepository.save(order);
        return orderMapper.toDto(order);
    }





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
