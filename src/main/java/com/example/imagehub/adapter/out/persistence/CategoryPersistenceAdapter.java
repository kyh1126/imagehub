package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.application.port.out.CategoryPort;
import com.example.imagehub.application.port.out.LoadCategoryPort;
import com.example.imagehub.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements LoadCategoryPort, CategoryPort {
    private final SpringDataCategoryRepository categoryRepository;

    @Override
    public void add(Category category) {
        CategoryJpaEntity categoryJpaEntity = CategoryJpaEntity.create(category);
        categoryRepository.save(categoryJpaEntity);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll().stream().map(Category::mapToDomainEntity).toList();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id).map(Category::mapToDomainEntity);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
