package com.kennel.backend.service;

import com.kennel.backend.dto.doctor.response.DoctorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    Page<DoctorResponseDto> getAll(Pageable pageable);

    Page<DoctorResponseDto> findByClinic(String clinicSlug, Pageable pageable);

    Page<DoctorResponseDto> getById(Long id, Pageable pageable);
}
