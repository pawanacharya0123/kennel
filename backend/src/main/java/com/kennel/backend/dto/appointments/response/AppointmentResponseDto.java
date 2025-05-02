package com.kennel.backend.dto.appointments.response;

import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.dto.dog.response.DogResponseDTO;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.Clinic;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.AppointmentStatus;
import lombok.Builder;

import java.util.Date;

@Builder
public class AppointmentResponseDto {
    private Date appointmentTime;
    private AppointmentStatus status;
    private UserDetailsResponseDto owner;
    private DogResponseDTO dog;
    private UserDetailsResponseDto doctor;
    private ClinicResponseDto clinic;
    private String slug;
    private String note;
}
