package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.ImageUseCase;
import com.example.imagehub.application.port.out.ImageRepositoryPort;
import com.example.imagehub.domain.model.ImageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService implements ImageUseCase {

    private final ImageRepositoryPort imageRepository;
    private final Set<String> validCategories = Set.of("PERSON", "LANDSCAPE", "ANIMAL", "FOOD", "OTHERS");
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void uploadImage(MultipartFile file, String description, List<String> categories) {
        validateCategories(categories);
        String fileName = saveFile(file);
        ImageModel image = new ImageModel(null, fileName, description, categories);
        imageRepository.save(image);
    }

    @Override
    public List<ImageModel> getImages() {
        return imageRepository.findAll();
    }

    @Override
    public ImageModel getImage(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @Override
    public List<String> getImageCategories(Long id) {
        return imageRepository.findCategoriesById(id);
    }

    @Override
    public void deleteImage(Long id) {
        ImageModel image = getImage(id);
        deleteFile(image.getFileName());
        imageRepository.deleteById(id);
    }

    @Override
    public void addCategoriesToImage(Long id, List<String> categories) {
        validateCategories(categories);
        ImageModel image = getImage(id);
        image.getCategories().addAll(categories);
        imageRepository.save(image);
    }

    @Override
    public void removeCategoriesFromImage(Long id, List<String> categories) {
        validateCategories(categories);
        ImageModel image = getImage(id);
        image.getCategories().removeAll(categories);
        imageRepository.save(image);
    }

    private void validateCategories(List<String> categories) {
        for (String category : categories) {
            if (!validCategories.contains(category)) {
                throw new IllegalArgumentException("Invalid category: " + category);
            }
        }
    }

    String saveFile(MultipartFile file) {
        try {
            Files.createDirectories(Path.of(uploadDir));
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(uploadDir, uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    void deleteFile(String fileName) {
        try {
            Path filePath = Path.of(uploadDir, fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
