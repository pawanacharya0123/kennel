package com.kennel.backend.dto.clinic;

import com.kennel.backend.dto.clinic.request.ClinicRequestDto;
import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.entity.Clinic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClinicDtoMapper {
    private final UserEntityDtoMapper userEntityDtoMapper;

    public ClinicResponseDto toDto(Clinic clinic){
        return ClinicResponseDto.builder()
                .name(clinic.getName())
                .address(clinic.getAddress())
                .manager(userEntityDtoMapper.toDto(clinic.getManager()))
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

    public Page<ClinicResponseDto> toDto(Page<Clinic> clinics){
        return clinics.map(this::toDto);
    }
}
