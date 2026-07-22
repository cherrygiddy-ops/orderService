package com.orderservice.system.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductsMapper {

    ProductsEntity toEntity(ProductsRequestDto productsRequestDto);

    // Map directly from categoryId string field
    @Mapping(target = "categoryId", source = "categoryId")
    ProductsResponseDto toDto(ProductsEntity product);

    void updateProduct(UpdateProductRequest request, @MappingTarget ProductsEntity productsEntity);
    ProductPageResponse toPageResponse(Page<ProductsEntity> page);
}
