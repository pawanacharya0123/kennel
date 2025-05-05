package com.kennel.backend.repository;

import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.VaccineRecord;
import com.kennel.backend.entity.VetVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRecordRepository extends JpaRepository<VaccineRecord, Long> {
    Page<VaccineRecord> findByDog(Dog dog, Pageable pageable);
    Page<VaccineRecord> findByDoctor(UserEntity doctor, Pageable pageable);
    Page<VaccineRecord> findByVetVisit(VetVisit vetVisit, Pageable pageable);
    Page<VaccineRecord> findByClinic(String clinicSlug, Pageable pageable);
    Page<VaccineRecord> findByVaccine(String vaccineSlug, Pageable pageable);
}
