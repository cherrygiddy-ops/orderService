package com.orderservice.system.products;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "product_images")
public class ProductImage {

    @Id
    private String id;   // MongoDB usually uses String/ObjectId

    private String imageUrl;

    // Option 1: store productId reference
    private String productId;
}
