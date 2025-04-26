package com.kennel.backend.repository;

import com.kennel.backend.entity.Kennel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KennelRepository extends JpaRepository<Kennel, Long> {
}
