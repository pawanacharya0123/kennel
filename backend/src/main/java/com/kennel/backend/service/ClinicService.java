package com.kennel.backend.service;

import com.kennel.backend.dto.clinic.request.ClinicRequestDto;
import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface ClinicService {
    List<ClinicResponseDto> getAll();
    ClinicResponseDto getClinicBySlug(String slug);
    ClinicResponseDto createClinic(ClinicRequestDto clinicRequestDto);
    ClinicResponseDto changeManager(String slug, Long userId);
    ClinicResponseDto updateClinic(String slug, ClinicRequestDto clinicRequestDto);
    Set<UserEntity> getDoctors(String slug);
    UserEntity getManager(String slug);
}
