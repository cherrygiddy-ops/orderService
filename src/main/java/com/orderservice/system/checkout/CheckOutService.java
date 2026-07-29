package com.orderservice.system.checkout;

import com.orderservice.system.cart.CartNotFoundException;
import com.orderservice.system.cart.CartRepository;
import com.orderservice.system.cart.CartService;
import com.orderservice.system.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;

@RequiredArgsConstructor
@Service
public class CheckOutService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final SequenceGeneratorService sequenceGenerator;
    private final CartService cartService;

    public CheckoutResponseDto placeOrder(ObjectId cartId, String phoneNumber) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);

        if (cart.isEmpty()) throw new CartEmptyException();

        var order = OrderEntity.createOrder(cart, phoneNumber);
        order.setOrderId(sequenceGenerator.generateSequence("orders_sequence"));
        order.setPaymentStatus(PaymentStatus.PENDING);
        orderRepository.save(order);
        cartService.clearCart(order.getCart().getId());
        return new CheckoutResponseDto(order.getOrderId(), null, phoneNumber);
    }


}
