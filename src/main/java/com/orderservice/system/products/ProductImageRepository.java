package com.orderservice.system.products;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends MongoRepository<ProductImage, String> {
    List<ProductImage> findByProductId(String productId);
}
