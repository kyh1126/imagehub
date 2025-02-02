package com.example.imagehub.domain;

import com.example.imagehub.adapter.out.persistence.CategoryJpaEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Category {

    private Long id;
    private String name;

    public static Category mapToDomainEntity(CategoryJpaEntity entity) {
        return new Category(entity.getId(), entity.getName());
    }

    public static Category of(String name) {
        return new Category(null, name);
    }
}
