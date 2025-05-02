package com.kennel.backend.dto.vaccine;

import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.dto.vaccine.request.VaccineRequestDto;
import com.kennel.backend.dto.vaccine.response.VaccineResponseDto;
import com.kennel.backend.entity.Vaccine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VaccineDtoMapper {
    private final UserEntityDtoMapper userEntityDtoMapper;

    public VaccineResponseDto toDto(Vaccine vaccine){
        return VaccineResponseDto.builder()
                .name(vaccine.getName())
                .description(vaccine.getDescription())
                .slug(vaccine.getSlug())
                .creator(userEntityDtoMapper.toDto(vaccine.getVaccineCreator()))
                .build();
    }

    public List<VaccineResponseDto> toDto(List<Vaccine> vaccines){
        return vaccines.stream()
                .map(this::toDto).toList();
    }

    public Vaccine toEntity(VaccineRequestDto vaccineRequestDto){
        return Vaccine.builder()
                .name(vaccineRequestDto.getName())
                .description(vaccineRequestDto.getDescription())
                .build();
    }

}
