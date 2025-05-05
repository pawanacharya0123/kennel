package com.kennel.backend.repository;

import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.VetVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VetVisitRepository extends JpaRepository<VetVisit, Long> {
    Page<VetVisit> findByOwner(UserEntity currentAuthUser, Pageable pageable);
    Page<VetVisit> findByDog(Dog dog, Pageable pageable);
}
