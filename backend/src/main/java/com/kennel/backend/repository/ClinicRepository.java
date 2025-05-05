package com.kennel.backend.repository;

import com.kennel.backend.entity.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findBySlug(String slug);

    Page<Clinic> findByDeletedFalse(Pageable pageable);

    Optional<Clinic> findBySlugAndDeletedFalse(String slug);

    boolean existsBySlug(String slug);

    boolean existsByNameAndAddress(String name, String address);
}
