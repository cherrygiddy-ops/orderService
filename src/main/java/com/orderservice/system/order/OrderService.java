package com.orderservice.system.order;

import com.orderservice.system.checkout.CheckoutResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderResponseDto getOrderById(Long orderId) {
        var order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException());
        return orderMapper.toDto(order);
    }

}
