package com.kennel.backend.dto.vaccineRecord.response;

import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.dto.doctor.response.DoctorResponseDto;
import com.kennel.backend.dto.dog.response.DogResponseDTO;
import com.kennel.backend.dto.vaccine.response.VaccineResponseDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;
import lombok.Builder;

import java.util.Date;

@Builder
public class VaccineRecordResponseDto {
    private VaccineResponseDto vaccine;
    private DogResponseDTO dog;
    private VetVisitResponseDto vetVisit;
    private ClinicResponseDto clinic;
    private DoctorResponseDto doctor;
    private Date vaccinationDate;
}
