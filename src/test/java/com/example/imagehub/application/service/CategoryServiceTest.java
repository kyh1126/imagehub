package com.example.imagehub.application.service;

import com.example.imagehub.application.port.out.CategoryRepositoryPort;
import com.example.imagehub.domain.model.CategoryModel;
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
    private CategoryRepositoryPort categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCategory() {
        CategoryModel category = new CategoryModel(null, "TEST");
        doNothing().when(categoryRepository).save(category);

        categoryService.addCategory("TEST");

        verify(categoryRepository, times(1)).save(any(CategoryModel.class));
    }

    @Test
    void testGetCategories() {
        List<CategoryModel> categories = List.of(new CategoryModel(1L, "PERSON"), new CategoryModel(2L, "ANIMAL"));
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryModel> result = categoryService.getCategories();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
