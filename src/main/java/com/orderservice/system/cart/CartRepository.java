package com.orderservice.system.cart;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

import org.bson.types.ObjectId;

public interface CartRepository extends MongoRepository<CartEntity, ObjectId> {

    // Fetch cart by ID — items are embedded, so they come along automatically
    Optional<CartEntity> findById(ObjectId cartId);

    // Example: fetch cart by userId if you add userId field in CartEntity
    Optional<CartEntity> findByUserId(String userId);
}
