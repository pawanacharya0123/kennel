package com.kennel.backend.dto.doctor.response;

import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.entity.Clinic;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public class DoctorResponseDto {
    private String email;
    private String extraInfo;
    private List<ClinicResponseDto> clinics;
}
