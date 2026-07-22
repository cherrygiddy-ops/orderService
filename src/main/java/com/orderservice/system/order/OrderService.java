package com.orderservice.system.order;

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

}
