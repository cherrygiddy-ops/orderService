package com.orderservice.system.products;

import com.orderservice.system.categories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductsRepository productsRepository;
    private final ProductsMapper productsMapper;

    // Add product (no image upload)
    public ProductsResponseDto addProduct(ProductsRequestDto requestDto) {
        var category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        var product = productsMapper.toEntity(requestDto);
        product.setCategoryId(category.getId().toString());

        productsRepository.save(product);
        return productsMapper.toDto(product);
    }

    // Get product details by ID
    public ProductsResponseDto getProductsDetails(String id) {
        var product = productsRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productsMapper.toDto(product);
    }

    // Get product details by name
    public ProductsResponseDto getProductsDetailsByName(String name) {
        var product = productsRepository.findByName(name).orElseThrow(ProductNotFoundException::new);
        return productsMapper.toDto(product);
    }

    // Get all products (with optional sort + category filter)
    public List<ProductsResponseDto> getAllProducts(Byte categoryId, String sortBy) {
        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "name";
        Sort sort = Sort.by(sortField);

        if (categoryId != null) {
            return productsRepository.findByCategoryId(categoryId.toString(), sort)
                    .stream()
                    .map(productsMapper::toDto)
                    .toList();
        }

        return productsRepository.findAll(sort)
                .stream()
                .map(productsMapper::toDto)
                .toList();
    }

    // Search products by keyword + optional category + sort
    public List<ProductsResponseDto> searchProducts(Byte categoryId, String keyword, String sortBy) {
        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "name";
        Sort sort = Sort.by(sortField);

        if (categoryId != null && keyword != null && !keyword.isBlank()) {
            return productsRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId.toString(), keyword, sort)
                    .stream()
                    .map(productsMapper::toDto)
                    .toList();
        } else if (categoryId != null) {
            return productsRepository.findByCategoryId(categoryId.toString(), sort)
                    .stream()
                    .map(productsMapper::toDto)
                    .toList();
        } else if (keyword != null && !keyword.isBlank()) {
            return productsRepository.findByNameContainingIgnoreCase(keyword, sort)
                    .stream()
                    .map(productsMapper::toDto)
                    .toList();
        }

        return productsRepository.findAll(sort)
                .stream()
                .map(productsMapper::toDto)
                .toList();
    }

    // Delete product
    public void deleteProduct(String id) {
        var product = productsRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productsRepository.delete(product);
    }

    // Update product
    public ProductsResponseDto updateProduct(String id, UpdateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        var product = productsRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        product.setName(request.getName());
        product.setDescriptions(request.getDescriptions());
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice());
        product.setCategoryId(category.getId().toString());

        productsRepository.save(product);
        return productsMapper.toDto(product);
    }
}
