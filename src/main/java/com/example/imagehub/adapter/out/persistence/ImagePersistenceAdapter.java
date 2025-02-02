package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.application.port.out.LoadImagePort;
import com.example.imagehub.application.port.out.UpdateImagePort;
import com.example.imagehub.application.port.out.UploadImagePort;
import com.example.imagehub.common.PersistenceAdapter;
import com.example.imagehub.domain.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@PersistenceAdapter
public class ImagePersistenceAdapter implements UploadImagePort, LoadImagePort, UpdateImagePort {

    private final SpringDataImageRepository imageRepository;

    @Override
    public void upload(Image image) {
        ImageJpaEntity imageJpaEntity = ImageJpaEntity.create(image);

        imageRepository.save(imageJpaEntity);
    }

    @Override
    public void update(Image image) {
        var existingEntity = imageRepository.findById(image.getId())
                .orElseThrow(() -> new RuntimeException("Image with ID " + image.getId() + " not found"));
        existingEntity.update(image);
        imageRepository.save(existingEntity);
    }

    @Override
    public List<Image> getImages(Pageable pageable) {
        return imageRepository.findAll(pageable).stream().map(Image::mapToDomainEntity).toList();
    }

    @Override
    public Image getImage(Long id) {
        return imageRepository.findById(id).map(Image::mapToDomainEntity)
                .orElseThrow(() -> new RuntimeException("Image not found"));
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
