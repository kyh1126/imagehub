package com.example.imagehub.domain.model;

import com.example.imagehub.adapter.out.persistence.ImageJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageModel {
    private Long id;
    private String fileName;
    private String description;
    private List<String> categories;
    private String filePath;       // 원본 이미지 경로
    private String thumbnailPath;  // 썸네일 이미지 경로

    public static ImageModel of(ImageJpaEntity entity) {
        return new ImageModel(
                entity.getId(),
                entity.getFileName(),
                entity.getDescription(),
                entity.getCategories(),
                entity.getFilePath(),
                entity.getThumbnailPath()
        );
    }
}
