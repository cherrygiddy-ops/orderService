package com.orderservice.system.products;

import lombok.Data;

import java.util.List;

@Data
public class ProductPageResponse {
        private List<ProductsResponseDto> content;
        private int totalPages;
        private long totalElements;
        private int pageNumber;
        private int pageSize;

        // getters and setters

}
