package com.example.imagehub.adapter.in;

import com.example.imagehub.application.port.in.CategoryUseCase;
import com.example.imagehub.common.WebAdapter;
import com.example.imagehub.domain.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Category", description = "카테고리 관리 API")
@SecurityRequirement(name = "Bearer Authentication")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "카테고리 추가")
    @PostMapping
    public Map<String, String> addCategory(@RequestBody String name) {
        categoryUseCase.addCategory(name);
        return Map.of("message", "Category added successfully");
    }

    @Operation(summary = "카테고리 목록 조회")
    @GetMapping
    public List<Category> getCategories() {
        return categoryUseCase.getCategories();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("{id}")
    public Map<String, String> deleteCategory(@PathVariable Long id) {
        categoryUseCase.deleteCategory(id);
        return Map.of("message", "Category deleted successfully");
    }
}
