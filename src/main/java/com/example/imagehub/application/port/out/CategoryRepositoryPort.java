package com.example.imagehub.application.port.out;

import com.example.imagehub.domain.model.CategoryModel;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {
    void save(CategoryModel category);

    List<CategoryModel> findAll();

    Optional<CategoryModel> findById(Long id);

    void deleteById(Long id);
}
