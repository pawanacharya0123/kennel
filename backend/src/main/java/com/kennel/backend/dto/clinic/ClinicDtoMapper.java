package com.kennel.backend.dto.clinic;

import com.kennel.backend.dto.clinic.request.ClinicRequestDto;
import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.entity.Clinic;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClinicDtoMapper {
    public ClinicResponseDto toDto(Clinic clinic){
        return ClinicResponseDto.builder()
                .name(clinic.getName())
                .address(clinic.getAddress())
                .manager(clinic.getManager())
                .slug(clinic.getSlug())
                .build();
    }

    public Clinic toEntity(ClinicRequestDto clinicRequestDto){
        return Clinic.builder()
                .name(clinicRequestDto.getName())
                .address(clinicRequestDto.getAddress())
                .build();
    }

    public List<ClinicResponseDto> toDto(List<Clinic> clinics){
        return clinics.stream()
                .map(this::toDto)
                .toList();
    }
}
