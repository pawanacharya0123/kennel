package com.kennel.backend.repository;

import com.kennel.backend.entity.VetVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetVisitRepository extends JpaRepository<VetVisit, Long> {
}
