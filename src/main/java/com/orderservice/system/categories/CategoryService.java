package com.orderservice.system.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private  final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public void addCategory(CategoryRequestDto requestDto) {
        var categoryEntity = mapper.toEntity(requestDto);
        categoryRepository.save(categoryEntity);
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Bean
    CommandLineRunner seedCategories(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.save(new CategoryEntity((byte) 1, "Electronics"));
                categoryRepository.save(new CategoryEntity((byte) 2, "Books"));
            }
        };
    }

}
