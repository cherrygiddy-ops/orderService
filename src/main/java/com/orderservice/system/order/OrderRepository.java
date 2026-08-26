package com.orderservice.system.order;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(Long orderId);

    // Load all orders for a given customer
    List<OrderEntity> findByCustomerId(UUID customerId);

    // Example: custom query to fetch orders with items by customer
    @Query("{ 'customer.id' : ?0 }")
    List<OrderEntity> loadAllOrderForCustomerWithItems(UUID customerId);

    // Update payment status (Mongo doesn’t support JPQL updates directly)
    // Instead, you fetch the order, update the field, and save it back in service layer


        List<OrderEntity> findTop7ByOrderDateBetweenAndPaymentStatus(
                LocalDateTime startOfDay,
                LocalDateTime endOfDay,
                String paymentStatus
        );
    long countByPaymentStatusAndOrderDateBetween(
            String status, Date start, Date end);

    List<OrderEntity> findByPaymentStatusAndOrderDateBetween(
            String status, Date start, Date end);

}
