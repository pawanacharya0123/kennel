package com.kennel.backend.repository;

import com.kennel.backend.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    Optional<Vaccine> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}
