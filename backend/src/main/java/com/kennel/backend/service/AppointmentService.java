package com.kennel.backend.service;

import com.kennel.backend.dto.appointments.request.AppointmentRequestDto;
import com.kennel.backend.dto.appointments.response.AppointmentResponseDto;
import com.kennel.backend.entity.enums.AppointmentStatus;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    List<AppointmentResponseDto> getAll();

    List<AppointmentResponseDto> getAllAppointmentsByDog(String slug);

    AppointmentResponseDto makeAppointment(AppointmentRequestDto appointmentRequestDto);

    AppointmentResponseDto updateAppointmentTime(Long id, Date date);

    AppointmentResponseDto updateAppointmentStatus(Long id, AppointmentStatus status);
}
