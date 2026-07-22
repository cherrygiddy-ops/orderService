package com.orderservice.system.products;

import com.orderservice.system.categories.CategoryEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "products")
public class ProductsEntity {

    @Id
    private String id;   // MongoDB usually uses String/ObjectId

    private String name;
    private String descriptions;
    private Integer quantity;
    private BigDecimal price;

    // Option 1: single image URL
    private String imageUrl;

    // Option 2: multiple images
    // private List<String> imageUrls;

    // Reference to category (store categoryId or embed category snapshot)
    private String categoryId;
}
