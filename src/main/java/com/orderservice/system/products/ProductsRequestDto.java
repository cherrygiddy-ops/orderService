package com.orderservice.system.products;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductsRequestDto {
    private Integer id;
    private String name;
    private String descriptions;
    private Integer quantity;
    private BigDecimal price;
    private String categoryId;

}
