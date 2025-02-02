package com.example.imagehub.application.port.in;

import com.example.imagehub.domain.Category;

import java.util.List;

public interface CategoryUseCase {

    void addCategory(String name);

    List<Category> getCategories();

    void deleteCategory(Long id);

}
