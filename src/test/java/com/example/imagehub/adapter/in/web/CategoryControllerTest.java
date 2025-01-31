package com.example.imagehub.adapter.in.web;

import com.example.imagehub.application.port.in.CategoryUseCase;
import com.example.imagehub.domain.model.CategoryModel;
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
        List<CategoryModel> categories = List.of(
                new CategoryModel(1L, "PERSON"),
                new CategoryModel(2L, "ANIMAL")
        );
        when(categoryUseCase.getCategories()).thenReturn(categories);

        List<CategoryModel> response = categoryController.getCategories();

        assertEquals(2, response.size());
        verify(categoryUseCase, times(1)).getCategories();
    }

    @Test
    void testAddCategory() {
        doNothing().when(categoryUseCase).addCategory("FOOD");

        Map<String, String> response = categoryController.addCategory("FOOD");

        assertEquals("Category added successfully", response.get("message"));
        verify(categoryUseCase, times(1)).addCategory("FOOD");
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryUseCase).deleteCategory(1L);

        Map<String, String> response = categoryController.deleteCategory(1L);

        assertEquals("Category deleted successfully", response.get("message"));
        verify(categoryUseCase, times(1)).deleteCategory(1L);
    }
}
