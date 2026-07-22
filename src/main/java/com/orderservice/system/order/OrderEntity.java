package com.orderservice.system.order;

import com.orderservice.system.cart.CartEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "orders")
public class OrderEntity {

    @Id
    private UUID orderId;
    private String customerId;

    private LocalDateTime orderDate = LocalDateTime.now();

    private PaymentStatus paymentStatus;
    private DeliveryStatus deliveryStatus;

    private String comments;
    private LocalDateTime shippedDate;
    private BigDecimal totalPrice;

    private Shipper shipper;          // embed or reference
    private CartEntity cart;          // embed cart snapshot

    private Set<OrderItemsEntity> orderItems = new LinkedHashSet<>();

    public void addItems(OrderItemsEntity item) {
        orderItems.add(item);
    }

    public static OrderEntity createOrder(CartEntity cart, String phoneNumber) {
        var order = new OrderEntity();
        order.setCart(cart);
        order.setComments("order 1");
        order.setTotalPrice(cart.getTotalPrice());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setDeliveryStatus(DeliveryStatus.PROCESSING);

        cart.getItems().forEach(cartI -> {
            var orderItem = new OrderItemsEntity(cartI.getProduct(), cartI.getQuantity());
            order.addItems(orderItem);
        });

        return order;
    }

}
