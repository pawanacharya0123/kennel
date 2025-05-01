package com.kennel.backend.dto.appointments.response;

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
    private UserEntity owner;
    private Dog dog;
    private UserEntity doctor;
    private Clinic clinic;
    private String slug;
    private String note;
}
