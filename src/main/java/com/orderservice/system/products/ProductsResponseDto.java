package com.orderservice.system.products;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductsResponseDto {
    private String id;              // ✅ changed from Integer to String
    private String name;
    private String descriptions;
    private Integer quantity;
    private BigDecimal price;
    private Byte categoryId;
}
