package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.CategoryUseCase;
import com.example.imagehub.application.port.out.CategoryRepositoryPort;
import com.example.imagehub.domain.model.CategoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;

    @Override
    public void addCategory(String name) {
        categoryRepository.save(new CategoryModel(null, name));
    }

    @Override
    public List<CategoryModel> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
