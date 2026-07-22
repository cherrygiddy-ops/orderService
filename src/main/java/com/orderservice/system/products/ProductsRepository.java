package com.orderservice.system.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends MongoRepository<ProductsEntity, String> {

    Optional<ProductsEntity> findByName(String name);

    Optional<ProductsEntity> findById(String id);

    List<ProductsEntity> findTop5ByNameLike(String name);

    List<ProductsEntity> findByPriceBetween(BigDecimal min, BigDecimal max);

    // ✅ Use Sort instead of Pageable
    List<ProductsEntity> findByCategoryId(String categoryId, Sort sort);

    List<ProductsEntity> findByNameContainingIgnoreCase(String keyword, Sort sort);

    List<ProductsEntity> findByCategoryIdAndNameContainingIgnoreCase(String categoryId, String keyword, Sort sort);
}
