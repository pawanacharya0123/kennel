package com.kennel.backend.repository;

import com.kennel.backend.entity.Kennel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KennelRepository extends JpaRepository<Kennel, Long> {
    List<Kennel> findByOwnerId(Long ownerId);

    Optional<Kennel> findBySlug(String slug);

    Boolean existsBySlug(String slug);

    Boolean existsByNameAndLocation(String name, String location);

    void deleteBySlug(String slug);
}
