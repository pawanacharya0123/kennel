package com.kennel.backend.repository;

import com.kennel.backend.entity.Appointment;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByOwner(UserEntity currentAuthUser);
    List<Appointment> findByDog(Dog dog);

    boolean existsBySlug(String slug);
}
