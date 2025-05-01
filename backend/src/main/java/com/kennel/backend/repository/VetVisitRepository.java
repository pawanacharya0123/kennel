package com.kennel.backend.repository;

import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.VetVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VetVisitRepository extends JpaRepository<VetVisit, Long> {
    List<VetVisit> findByOwner(UserEntity currentAuthUser);
}
