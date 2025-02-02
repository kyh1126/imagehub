package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.domain.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class ImageJpaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column
    private String description;

    @ElementCollection
    @CollectionTable(name = "image_categories", joinColumns = @JoinColumn(name = "image_id"))
    @Column(name = "category")
    private List<String> categories;

    @Column(nullable = false) // 원본 이미지 경로 추가
    private String filePath;

    @Column(nullable = false) // 썸네일 이미지 경로 추가
    private String thumbnailPath;

    public static ImageJpaEntity create(Image model) {
        ImageJpaEntity imageJpaEntity = new ImageJpaEntity();

        imageJpaEntity.fileName = model.getFileName();
        imageJpaEntity.description = model.getDescription();
        imageJpaEntity.categories = model.getCategories();
        imageJpaEntity.filePath = model.getFilePath();
        imageJpaEntity.thumbnailPath = model.getThumbnailPath();

        return imageJpaEntity;
    }

    public void update(Image model) {
        this.fileName = model.getFileName();
        this.description = model.getDescription();
        this.categories = model.getCategories();
        this.filePath = model.getFilePath();
        this.thumbnailPath = model.getThumbnailPath();
    }
}
