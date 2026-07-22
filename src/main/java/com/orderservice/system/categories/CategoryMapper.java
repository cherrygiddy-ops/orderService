package com.orderservice.system.categories;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryEntity toEntity (CategoryRequestDto requestDto);
}
