package com.kennel.backend.controller;

import com.kennel.backend.dto.appointments.request.AppointmentRequestDto;
import com.kennel.backend.dto.appointments.response.AppointmentResponseDto;
import com.kennel.backend.entity.enums.AppointmentStatus;
import com.kennel.backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments(){
        return ResponseEntity.ok(appointmentService.getAll());
    }

    @GetMapping("/dog/{dogSlug}")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentsByDog(@PathVariable String dogSlug){
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByDog(dogSlug));
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> makeAppointment(@RequestBody AppointmentRequestDto appointmentRequestDto){
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
