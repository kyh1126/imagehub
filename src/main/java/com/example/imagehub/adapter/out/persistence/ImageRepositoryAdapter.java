package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.application.port.out.ImagePort;
import com.example.imagehub.domain.model.ImageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ImageRepositoryAdapter implements ImagePort {
    private final SpringDataImageRepository imageRepository;

    @Override
    public void create(ImageModel imageModel) {
        ImageJpaEntity imageJpaEntity = ImageJpaEntity.create(imageModel);
        imageRepository.save(imageJpaEntity);
    }

    @Override
    public void update(ImageModel imageModel) {
        var existingEntity = imageRepository.findById(imageModel.getId())
                .orElseThrow(() -> new RuntimeException("Image with ID " + imageModel.getId() + " not found"));
        existingEntity.update(imageModel);
        imageRepository.save(existingEntity);
    }

    @Override
    public List<ImageModel> findAll() {
        return imageRepository.findAll().stream().map(ImageModel::of).toList();
    }

    @Override
    public Optional<ImageModel> findById(Long id) {
        return imageRepository.findById(id).map(ImageModel::of);
    }

    @Override
    public void deleteById(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public List<String> findCategoriesById(Long id) {
        return imageRepository.findCategoriesById(id);
    }
}
