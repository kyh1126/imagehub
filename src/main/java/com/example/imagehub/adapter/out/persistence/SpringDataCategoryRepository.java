package com.example.imagehub.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataCategoryRepository extends JpaRepository<CategoryJpaEntity, Long> {
}
