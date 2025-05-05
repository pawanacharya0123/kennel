package com.kennel.backend.repository;

import com.kennel.backend.entity.Appointment;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByOwner(UserEntity currentAuthUser, Pageable pageable);
    Page<Appointment> findByDog(Dog dog, Pageable pageable);

    boolean existsBySlug(String slug);
}
