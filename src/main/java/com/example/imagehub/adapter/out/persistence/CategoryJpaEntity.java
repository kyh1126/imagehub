package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.domain.model.CategoryModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class CategoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public static CategoryJpaEntity create(CategoryModel model) {
        CategoryJpaEntity categoryJpaEntity = new CategoryJpaEntity();

        categoryJpaEntity.name = model.getName();

        return categoryJpaEntity;
    }
}
