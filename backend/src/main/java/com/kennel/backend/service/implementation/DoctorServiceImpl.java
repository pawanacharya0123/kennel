package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.doctor.DoctorDtoMapper;
import com.kennel.backend.dto.doctor.response.DoctorResponseDto;
import com.kennel.backend.entity.Clinic;
import com.kennel.backend.entity.enums.RoleName;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.ClinicRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorDtoMapper doctorDtoMapper;
    private final UserEntityRepository userEntityRepository;
    private final ClinicRepository clinicRepository;
    @Override
    public Page<DoctorResponseDto> getAll(Pageable pageable) {
        return doctorDtoMapper.toDto(userEntityRepository.findByRoles(RoleName.ROLE_DOCTOR, pageable));
    }

    @Override
    public Page<DoctorResponseDto> findByClinic(String clinicSlug, Pageable pageable) {
        Clinic clinic = clinicRepository.findBySlug(clinicSlug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", clinicSlug));

        return doctorDtoMapper.toDto(
                userEntityRepository.findByClinicsAndRoles(clinic, RoleName.ROLE_DOCTOR,pageable)
        );
    }

    @Override
    public Page<DoctorResponseDto> getById(Long id, Pageable pageable) {
        return doctorDtoMapper.toDto(userEntityRepository.findByIdAndRoles(id, RoleName.ROLE_DOCTOR, pageable));
    }
}
