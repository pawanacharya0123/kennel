package com.kennel.backend.repository;

import com.kennel.backend.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findBySlug(String slug);
}
