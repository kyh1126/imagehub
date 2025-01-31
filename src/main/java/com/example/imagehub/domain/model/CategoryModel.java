package com.example.imagehub.domain.model;

import com.example.imagehub.adapter.out.persistence.CategoryJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    private Long id;
    private String name;

    public static CategoryModel of(CategoryJpaEntity entity) {
        return new CategoryModel(entity.getId(), entity.getName());
    }
}
