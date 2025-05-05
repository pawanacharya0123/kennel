package com.kennel.backend.dto.vetVisit.response;

import com.kennel.backend.dto.appointments.response.AppointmentResponseDto;
import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.dto.doctor.response.DoctorResponseDto;
import com.kennel.backend.dto.dog.response.DogResponseDTO;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.*;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public class VetVisitResponseDto {
    private Long id;
    private String notes;
    private String prescription;
    private String slug;
    private Date visitDate;
    private DogResponseDTO dog;
    private AppointmentResponseDto appointment;
    private ClinicResponseDto clinic;
    private DoctorResponseDto doctor;
    private UserDetailsResponseDto owner;
}
