package com.example.imagehub.application.port.in;

import com.example.imagehub.domain.model.CategoryModel;

import java.util.List;

public interface CategoryUseCase {
    void addCategory(String name);

    List<CategoryModel> getCategories();

    void deleteCategory(Long id);
}
