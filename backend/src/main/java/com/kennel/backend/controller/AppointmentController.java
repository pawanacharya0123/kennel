package com.kennel.backend.controller;

import com.kennel.backend.dto.appointments.request.AppointmentRequestDto;
import com.kennel.backend.dto.appointments.response.AppointmentResponseDto;
import com.kennel.backend.entity.enums.AppointmentStatus;
import com.kennel.backend.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<Page<AppointmentResponseDto>> getAllAppointments(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(appointmentService.getAll(pageable));
    }

    @GetMapping("/dog/{dogSlug}")
    public ResponseEntity<Page<AppointmentResponseDto>> getAllAppointmentsByDog(
            @PathVariable String dogSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByDog(dogSlug, pageable));
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> makeAppointment(@RequestBody @Valid AppointmentRequestDto appointmentRequestDto){
        return ResponseEntity.ok(appointmentService.makeAppointment(appointmentRequestDto));
    }

    @PatchMapping("/{id}/time")
    public ResponseEntity<AppointmentResponseDto> updateAppointmentTime(@PathVariable Long id, @RequestParam Date date){
        return ResponseEntity.ok(appointmentService.updateAppointmentTime(id, date));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDto> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) throws BadRequestException {
        AppointmentStatus statusEnum;
        try {
            statusEnum = AppointmentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid appointment status value: " + status);
        }

        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, statusEnum));
    }

}
