package com.example.imagehub.application.port.out;

import com.example.imagehub.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ImageResponse {
    private Long id;
    private String fileName;
    private String description;
    private List<String> categories;
    private String filePath;
    private String thumbnailPath;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static ImageResponse from(Image image) {
        return new ImageResponse(
                image.getId(),
                image.getFileName(),
                image.getDescription(),
                image.getCategories(),
                image.getFilePath(),
                image.getThumbnailPath(),
                image.getCreatedAt(),
                image.getUpdatedAt()
        );
    }
}
