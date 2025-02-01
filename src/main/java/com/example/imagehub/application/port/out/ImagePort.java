package com.example.imagehub.application.port.out;

import com.example.imagehub.domain.model.ImageModel;

import java.util.List;
import java.util.Optional;

public interface ImagePort {
    void create(ImageModel image);

    void update(ImageModel image);

    List<ImageModel> findAll();

    Optional<ImageModel> findById(Long id);

    void deleteById(Long id);

    List<String> findCategoriesById(Long id);
}
