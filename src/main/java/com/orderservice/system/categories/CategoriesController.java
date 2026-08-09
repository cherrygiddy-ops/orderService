package com.orderservice.system.categories;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/categories")
public class CategoriesController {

    private final CategoryService categoryService;

    // ✅ Add category
    @PostMapping
    public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody String name) {
        return ResponseEntity.ok(categoryService.addCategory(name));
    }


    // ✅ Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // ✅ Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryDetails(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // ✅ Update category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable String id,
                                                              @RequestBody CategoryRequestDto requestDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, requestDto));
    }

    // ✅ Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

