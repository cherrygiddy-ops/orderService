package com.orderservice.system.categories;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDto toResponse(CategoryEntity entity);

}
