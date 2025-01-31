package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.application.port.out.ImageRepositoryPort;
import com.example.imagehub.domain.model.ImageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ImageRepositoryAdapter implements ImageRepositoryPort {
    private final SpringDataImageRepository imageRepository;

    @Override
    public void save(ImageModel imageModel) {
        ImageJpaEntity imageJpaEntity = ImageJpaEntity.of(imageModel);
        imageRepository.save(imageJpaEntity);
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
