package com.example.imagehub.adapter.in.web;

import com.example.imagehub.adapter.in.CategoryController;
import com.example.imagehub.application.port.in.CategoryUseCase;
import com.example.imagehub.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryUseCase categoryUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCategories() {
        List<Category> categories = List.of(
                Category.of("PERSON"),
                Category.of("ANIMAL")
        );
        when(categoryUseCase.getCategories()).thenReturn(categories);

        List<Category> response = categoryController.getCategories();

        assertEquals(2, response.size());
        verify(categoryUseCase, times(1)).getCategories();
    }

    @Test
    void testAddCategory() {
        doNothing().when(categoryUseCase).addCategory("FOOD2");

        Map<String, String> response = categoryController.addCategory("FOOD2");

        assertEquals("Category added successfully", response.get("message"));
        verify(categoryUseCase, times(1)).addCategory("FOOD2");
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryUseCase).deleteCategory(1L);

        Map<String, String> response = categoryController.deleteCategory(1L);

        assertEquals("Category deleted successfully", response.get("message"));
        verify(categoryUseCase, times(1)).deleteCategory(1L);
    }
}
