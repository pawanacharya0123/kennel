package com.kennel.backend.service;

import com.kennel.backend.dto.clinic.request.ClinicRequestDto;
import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.dto.doctor.response.DoctorResponseDto;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface ClinicService {
    Page<ClinicResponseDto> getAll(Pageable pageable);
    ClinicResponseDto getClinicBySlug(String slug);
    ClinicResponseDto createClinic(ClinicRequestDto clinicRequestDto);
    ClinicResponseDto changeManager(String slug, Long userId);
    ClinicResponseDto updateClinic(String slug, ClinicRequestDto clinicRequestDto);
    Page<DoctorResponseDto> getDoctors(String slug, Pageable pageable);
    UserDetailsResponseDto getManager(String slug);
}
