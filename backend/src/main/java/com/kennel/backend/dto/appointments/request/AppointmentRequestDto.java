package com.kennel.backend.dto.appointments.request;

import com.kennel.backend.entity.enums.AppointmentStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Date;

@Getter
public class AppointmentRequestDto {
    @NotNull
    private Date appointmentTime;
    @NotNull
    private AppointmentStatus status;
    @NotNull
    private String dog;
    @NotNull
    private String clinic;
    @NotNull
    private Long doctor;
    private String note;
}
