package com.example.imagehub.application.port.out;

import com.example.imagehub.domain.Category;

import java.util.List;
import java.util.Optional;

public interface LoadCategoryPort {

    List<Category> findAll();

    Optional<Category> findById(Long id);

}
