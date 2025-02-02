package com.example.imagehub;

import com.example.imagehub.application.service.CategoryService;
import com.example.imagehub.domain.Category;
import com.example.imagehub.infrastructure.config.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
class CategoryServiceIntegrationTest extends AbstractSpringBootTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void testAddAndRetrieveCategories() {
        categoryService.addCategory("TEST_CATEGORY");
        List<Category> categories = categoryService.getCategories();
        assertTrue(categories.stream().anyMatch(cat -> cat.getName().equals("TEST_CATEGORY")));
    }

    @Test
    void testDeleteCategory() {
        categoryService.addCategory("DELETE_CATEGORY");
        List<Category> categoriesBefore = categoryService.getCategories();
        Long categoryId = categoriesBefore.stream()
                .filter(cat -> cat.getName().equals("DELETE_CATEGORY"))
                .findFirst()
                .map(Category::getId)
                .orElseThrow();

        categoryService.deleteCategory(categoryId);
        List<Category> categoriesAfter = categoryService.getCategories();
        assertFalse(categoriesAfter.stream().anyMatch(cat -> cat.getId().equals(categoryId)));
    }
}
