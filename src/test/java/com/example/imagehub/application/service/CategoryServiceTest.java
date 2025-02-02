package com.example.imagehub.application.service;

import com.example.imagehub.application.port.out.CategoryPort;
import com.example.imagehub.application.port.out.LoadCategoryPort;
import com.example.imagehub.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private LoadCategoryPort loadCategoryPort;

    @Mock
    private CategoryPort categoryPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCategory() {
        Category category = Category.of("TEST");
        doNothing().when(categoryPort).add(category);

        categoryService.addCategory("TEST");

        verify(categoryPort, times(1)).add(any(Category.class));
    }

    @Test
    void testGetCategories() {
        List<Category> categories = List.of(Category.of("PERSON"), Category.of("ANIMAL"));
        when(loadCategoryPort.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getCategories();

        assertEquals(2, result.size());
        verify(loadCategoryPort, times(1)).findAll();
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryPort).delete(1L);

        categoryService.deleteCategory(1L);

        verify(categoryPort, times(1)).delete(1L);
    }
}
