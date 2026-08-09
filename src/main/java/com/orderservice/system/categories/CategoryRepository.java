package com.orderservice.system.categories;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {
    // Example derived query
    CategoryEntity findByName(String name);
}
