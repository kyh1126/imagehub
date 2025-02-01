package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.application.port.out.CategoryPort;
import com.example.imagehub.domain.model.CategoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryPort {
    private final SpringDataCategoryRepository categoryRepository;

    @Override
    public void create(CategoryModel category) {
        CategoryJpaEntity categoryJpaEntity = CategoryJpaEntity.create(category);
        categoryRepository.save(categoryJpaEntity);
    }

    @Override
    public List<CategoryModel> findAll() {
        return categoryRepository.findAll().stream().map(CategoryModel::of).toList();
    }

    @Override
    public Optional<CategoryModel> findById(Long id) {
        return categoryRepository.findById(id).map(CategoryModel::of);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
