package com.orderservice.system.order;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends MongoRepository<OrderEntity, UUID> {

    // Load all orders for a given customer
    List<OrderEntity> findByCustomerId(UUID customerId);

    // Example: custom query to fetch orders with items by customer
    @Query("{ 'customer.id' : ?0 }")
    List<OrderEntity> loadAllOrderForCustomerWithItems(UUID customerId);

    // Update payment status (Mongo doesn’t support JPQL updates directly)
    // Instead, you fetch the order, update the field, and save it back in service layer
}
