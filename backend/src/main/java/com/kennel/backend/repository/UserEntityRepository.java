package com.kennel.backend.repository;

import com.kennel.backend.entity.Clinic;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByClinics(Clinic clinic, Pageable pageable);

    Page<UserEntity> findByRoles(RoleName roleName, Pageable pageable);

    Page<UserEntity> findByClinicsAndRoles(Clinic clinic, RoleName roleName, Pageable pageable);

    Page<UserEntity> findByIdAndRoles(Long id, RoleName roleName, Pageable pageable);

    boolean existsByEmail(String email);
}
