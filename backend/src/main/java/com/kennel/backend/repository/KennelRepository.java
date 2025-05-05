package com.kennel.backend.repository;

import com.kennel.backend.entity.Kennel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KennelRepository extends JpaRepository<Kennel, Long> {
    Page<Kennel> findByOwnerId(Long ownerId, Pageable pageable);

    Optional<Kennel> findBySlug(String slug);

    Boolean existsBySlug(String slug);

    Boolean existsByNameAndLocation(String name, String location);

    void deleteBySlug(String slug);

    Optional<Kennel> findBySlugAndDeletedFalse(String slug);

    Optional<Kennel> findByIdAndDeletedFalse(Long kennelId);

    Page<Kennel> findByDeletedFalse(Pageable pageable);

    Page<Kennel> findByOwnerIdAndDeletedFalse(Long ownerId, Pageable pageable);
}
