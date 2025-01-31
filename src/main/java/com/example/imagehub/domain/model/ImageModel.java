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

    public static ImageModel of(ImageJpaEntity entity) {
        return new ImageModel(entity.getId(), entity.getFileName(), entity.getDescription(), entity.getCategories());
    }
}
