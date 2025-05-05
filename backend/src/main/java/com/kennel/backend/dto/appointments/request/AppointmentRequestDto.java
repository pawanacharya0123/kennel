package com.kennel.backend.dto.appointments.request;

import com.kennel.backend.entity.enums.AppointmentStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Date;

@Getter
public class AppointmentRequestDto {
    @NotNull
    private Date appointmentTime;
    @NotNull
    @NotBlank
    private AppointmentStatus status;
    @NotNull
    @NotBlank
    private String dog;
    @NotNull
    @NotBlank
    private String clinic;
    @NotNull
    @NotBlank
    private Long doctor;
    private String note;
}
