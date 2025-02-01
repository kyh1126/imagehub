package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.CategoryUseCase;
import com.example.imagehub.application.port.out.CategoryPort;
import com.example.imagehub.domain.model.CategoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {

    private final CategoryPort categoryPort;

    @Override
    public void addCategory(String name) {
        categoryPort.create(new CategoryModel(null, name));
    }

    @Override
    public List<CategoryModel> getCategories() {
        return categoryPort.findAll();
    }

    @Override
    public void deleteCategory(Long id) {
        categoryPort.deleteById(id);
    }
}
