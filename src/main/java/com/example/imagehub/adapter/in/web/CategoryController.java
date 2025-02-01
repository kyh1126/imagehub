package com.example.imagehub.adapter.in.web;

import com.example.imagehub.application.port.in.CategoryUseCase;
import com.example.imagehub.domain.model.CategoryModel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Category", description = "카테고리 관리 API")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Map<String, String> addCategory(@RequestParam("name") String name) {
        categoryUseCase.addCategory(name);
        return Map.of("message", "Category added successfully");
    }

    @GetMapping
    public List<CategoryModel> getCategories() {
        return categoryUseCase.getCategories();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Map<String, String> deleteCategory(@PathVariable Long id) {
        categoryUseCase.deleteCategory(id);
        return Map.of("message", "Category deleted successfully");
    }
}
