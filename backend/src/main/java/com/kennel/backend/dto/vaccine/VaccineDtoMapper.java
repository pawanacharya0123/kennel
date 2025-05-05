package com.kennel.backend.dto.vaccine;

import com.kennel.backend.dto.clinic.ClinicDtoMapper;
import com.kennel.backend.dto.doctor.DoctorDtoMapper;
import com.kennel.backend.dto.dog.DogDtoMapper;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.dto.vaccine.request.VaccineRequestDto;
import com.kennel.backend.dto.vaccineRecord.response.VaccineRecordResponseDto;
import com.kennel.backend.dto.vaccine.response.VaccineResponseDto;
import com.kennel.backend.dto.vetVisit.VetVisitDtoMapper;
import com.kennel.backend.entity.Vaccine;
import com.kennel.backend.entity.VaccineRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VaccineDtoMapper {
    private final UserEntityDtoMapper userEntityDtoMapper;


    public Vaccine toEntity(VaccineRequestDto vaccineRequestDto){
        return Vaccine.builder()
                .name(vaccineRequestDto.getName())
                .description(vaccineRequestDto.getDescription())
                .build();
    }

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


    public Page<VaccineResponseDto> toDto(Page<Vaccine> vaccines){
        return vaccines.map(this::toDto);
    }
}
