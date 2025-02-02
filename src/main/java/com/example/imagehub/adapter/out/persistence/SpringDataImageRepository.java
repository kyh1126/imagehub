package com.example.imagehub.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataImageRepository extends JpaRepository<ImageJpaEntity, Long> {

    @Query("SELECT c FROM ImageJpaEntity i JOIN i.categories c WHERE i.id = :id")
    List<String> findCategoriesById(@Param("id") Long id);

}
