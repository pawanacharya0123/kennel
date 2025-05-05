package com.kennel.backend.dto.vaccineRecord;

import com.kennel.backend.dto.clinic.ClinicDtoMapper;
import com.kennel.backend.dto.doctor.DoctorDtoMapper;
import com.kennel.backend.dto.dog.DogDtoMapper;
import com.kennel.backend.dto.vaccine.VaccineDtoMapper;
import com.kennel.backend.dto.vaccineRecord.response.VaccineRecordResponseDto;
import com.kennel.backend.dto.vetVisit.VetVisitDtoMapper;
import com.kennel.backend.entity.VaccineRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VaccineRecordDtoMapper {
    private final DogDtoMapper dogDtoMapper;
    private final ClinicDtoMapper clinicDtoMapper;
    private final DoctorDtoMapper doctorDtoMapper;
    private final VetVisitDtoMapper vetVisitDtoMapper;
    private final VaccineDtoMapper vaccineDtoMapper;

    public VaccineRecordResponseDto toDto(VaccineRecord vaccineRecord){
        return VaccineRecordResponseDto.builder()
                .dog(dogDtoMapper.toDto(vaccineRecord.getDog()))
                .clinic(clinicDtoMapper.toDto(vaccineRecord.getClinic()))
                .doctor(doctorDtoMapper.toDto(vaccineRecord.getDoctor()))
                .vaccine(vaccineDtoMapper.toDto(vaccineRecord.getVaccine()))
                .vaccinationDate(vaccineRecord.getVaccinationDate())
                .vetVisit(vetVisitDtoMapper.toDto(vaccineRecord.getVetVisit()))
                .build();
    }

    public Page<VaccineRecordResponseDto> toDto(Page<VaccineRecord> vaccineRecords){
        return vaccineRecords.map(this::toDto);
    }
}
