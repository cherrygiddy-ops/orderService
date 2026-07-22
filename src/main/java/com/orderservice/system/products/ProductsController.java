package com.orderservice.system.products;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("orders/products")
public class ProductsController {

    private final ProductService productService;

    // Add product (no image upload)
    @PostMapping
    public ResponseEntity<ProductsResponseDto> addProduct(@RequestBody ProductsRequestDto request) {
        var response = productService.addProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get product details by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductsResponseDto> getProductDetails(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.getProductsDetails(String.valueOf(productId)));
    }

    // ✅ Unified GET endpoint with category, keyword, and sort filters
    @GetMapping
    public ResponseEntity<List<ProductsResponseDto>> getProducts(
            @RequestParam(required = false) Byte categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy) {

        List<ProductsResponseDto> products;

        if (keyword != null && !keyword.isBlank()) {
            products = productService.searchProducts(categoryId, keyword, sortBy);
        } else {
            products = productService.getAllProducts(categoryId, sortBy);
        }

        return ResponseEntity.ok(products);
    }

    // Delete product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(String.valueOf(productId));
        return ResponseEntity.noContent().build();
    }

    // Update product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductsResponseDto> updateProduct(
            @PathVariable Integer productId,
            @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(String.valueOf(productId), request));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product not found");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("category not found");
    }
}
