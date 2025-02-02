package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.CategoryUseCase;
import com.example.imagehub.application.port.out.CategoryPort;
import com.example.imagehub.common.UseCase;
import com.example.imagehub.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@UseCase
@Transactional(readOnly = true)
public class CategoryService implements CategoryUseCase {

    private final CategoryPort categoryPort;

    @Override
    public void addCategory(String name) {
        categoryPort.add(Category.of(name));
    }

    @Override
    public List<Category> getCategories() {
        return categoryPort.findAll();
    }

    @Override
    public void deleteCategory(Long id) {
        categoryPort.deleteById(id);
    }
}
