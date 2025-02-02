package com.example.imagehub.adapter.out.persistence;

import com.example.imagehub.domain.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class CategoryJpaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public static CategoryJpaEntity create(Category model) {
        CategoryJpaEntity categoryJpaEntity = new CategoryJpaEntity();

        categoryJpaEntity.name = model.getName();

        return categoryJpaEntity;
    }
}
