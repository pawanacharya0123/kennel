package com.kennel.backend.repository;

import com.kennel.backend.entity.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DogRepository extends JpaRepository<Dog, Long> {
    Page<Dog> findByOwnerId(Long userId, Pageable pageable);
    Page<Dog> findByKennelId(Long kennelId, Pageable pageable);
    Boolean existsBySlug(String slug);

    boolean existsByNameAndOwnerId(String name, Long ownerId);

    Optional<Dog> findBySlug(String slug);

    Page<Dog> findByKennelSlug(String kennelSlug, Pageable pageable);
}
