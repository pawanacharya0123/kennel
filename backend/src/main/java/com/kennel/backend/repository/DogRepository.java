package com.kennel.backend.repository;

import com.kennel.backend.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findByOwnerId(Long userId);
    List<Dog> findByKennelId(Long kennelId);
    Boolean existsBySlug(String slug);

    boolean existsByNameAndOwnerId(String name, Long ownerId);

    Optional<Dog> findBySlug(String slug);

    List<Dog> findByKennelSlug(String kennelSlug);
}
