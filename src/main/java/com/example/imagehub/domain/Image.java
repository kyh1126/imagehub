package com.example.imagehub.domain;

import com.example.imagehub.adapter.out.persistence.ImageJpaEntity;
import com.example.imagehub.application.port.in.UploadImageCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image {

    private Long id;
    private String fileName;
    private String description;
    private List<String> categories;
    private String filePath;       // 원본 이미지 경로
    private String thumbnailPath;  // 썸네일 이미지 경로
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static Image mapToDomainEntity(ImageJpaEntity entity) {
        Image image = new Image();

        image.id = entity.getId();
        image.fileName = entity.getFileName();
        image.description = entity.getDescription();
        image.categories = new ArrayList<>(entity.getCategories()); // Lazy 로딩 문제 해결
        image.filePath = entity.getFilePath();
        image.thumbnailPath = entity.getThumbnailPath();
        image.createdAt = entity.getCreatedAt();
        image.updatedAt = entity.getUpdatedAt();

        return image;
    }

    public static Image of(
            UploadImageCommand uploadImageCommand, String fileName, String uploadDir, String thumbnailPath
    ) {
        Image image = new Image();

        image.fileName = fileName;
        image.description = uploadImageCommand.getDescription();
        image.categories = uploadImageCommand.getCategories();
        image.filePath = Path.of(uploadDir, fileName).toString();
        image.thumbnailPath = thumbnailPath;

        return image;
    }
}
