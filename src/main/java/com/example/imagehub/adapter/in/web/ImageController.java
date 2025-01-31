package com.example.imagehub.adapter.in.web;

import com.example.imagehub.application.port.in.ImageUseCase;
import com.example.imagehub.domain.model.ImageModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("images")
@Tag(name = "Image", description = "이미지 관리 API")
public class ImageController {

    private final ImageUseCase imageUseCase;

    @PostMapping
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file,
                                           @RequestParam("description") String description,
                                           @RequestParam("categories") List<String> categories) {
        imageUseCase.uploadImage(file, description, categories);
        return Map.of("message", "Image uploaded successfully");
    }

    @GetMapping
    public List<ImageModel> getImages() {
        return imageUseCase.getImages();
    }

    @GetMapping("/{id}")
    public ImageModel getImage(@PathVariable Long id) {
        return imageUseCase.getImage(id);
    }

    @GetMapping("/{id}/categories")
    public List<String> getImageCategories(@PathVariable Long id) {
        return imageUseCase.getImageCategories(id);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteImage(@PathVariable Long id) {
        imageUseCase.deleteImage(id);
        return Map.of("message", "Image deleted successfully");
    }

    @PostMapping("/{id}/categories")
    public Map<String, String> addCategoriesToImage(@PathVariable Long id, @RequestBody List<String> categories) {
        imageUseCase.addCategoriesToImage(id, categories);
        return Map.of("message", "Categories added successfully");
    }

    @DeleteMapping("/{id}/categories")
    public Map<String, String> removeCategoriesFromImage(@PathVariable Long id, @RequestBody List<String> categories) {
        imageUseCase.removeCategoriesFromImage(id, categories);
        return Map.of("message", "Categories removed successfully");
    }
}
