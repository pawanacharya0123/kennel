package com.kennel.backend.dto.doctor;

import com.kennel.backend.dto.clinic.ClinicDtoMapper;
import com.kennel.backend.dto.doctor.response.DoctorResponseDto;
import com.kennel.backend.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DoctorDtoMapper {
    private final ClinicDtoMapper clinicDtoMapper;

    public DoctorResponseDto toDto(UserEntity doctor){
        return DoctorResponseDto.builder()
                .email(doctor.getEmail())
                .extraInfo(doctor.getExtraInfo())
                .clinics(clinicDtoMapper.toDto(doctor.getClinics().stream().toList()))
                .build();
    }

    public List<DoctorResponseDto> toDto(List<UserEntity> doctors){
        return doctors.stream().map(this::toDto).toList();
    }

    public Page<DoctorResponseDto> toDto(Page<UserEntity> doctors){
        return doctors.map(this::toDto);
    }
}
