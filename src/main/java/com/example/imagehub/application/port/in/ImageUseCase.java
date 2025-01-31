package com.example.imagehub.application.port.in;

import com.example.imagehub.domain.model.ImageModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUseCase {
    void uploadImage(MultipartFile file, String description, List<String> categories);

    List<ImageModel> getImages();

    ImageModel getImage(Long id);

    List<String> getImageCategories(Long id);

    void deleteImage(Long id);

    void addCategoriesToImage(Long id, List<String> categories);

    void removeCategoriesFromImage(Long id, List<String> categories);
}
