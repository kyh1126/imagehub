package com.example.imagehub.application.port.in;

import com.example.imagehub.application.port.out.ImageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageUseCase {

    void uploadImage(UploadImageCommand uploadImageCommand);

    List<ImageResponse> getImages(Pageable pageable);

    ImageResponse getImage(Long id);

    List<String> getImageCategories(Long id);

    void deleteImage(Long id);

    void addCategoriesToImage(Long id, List<String> categories);

    void removeCategoriesFromImage(Long id, List<String> categories);

}
