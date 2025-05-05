package com.kennel.backend.service;

import com.kennel.backend.dto.appointments.request.AppointmentRequestDto;
import com.kennel.backend.dto.appointments.response.AppointmentResponseDto;
import com.kennel.backend.entity.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    Page<AppointmentResponseDto> getAll(Pageable pageable);

    Page<AppointmentResponseDto> getAllAppointmentsByDog(String slug, Pageable pageable);

    AppointmentResponseDto makeAppointment(AppointmentRequestDto appointmentRequestDto);

    AppointmentResponseDto updateAppointmentTime(Long id, Date date);

    AppointmentResponseDto updateAppointmentStatus(Long id, AppointmentStatus status);
}
