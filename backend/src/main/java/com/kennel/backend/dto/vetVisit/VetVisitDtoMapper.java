package com.kennel.backend.dto.vetVisit;

import com.kennel.backend.dto.vetVisit.request.VetVisitRequestDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;
import com.kennel.backend.entity.Appointment;
import com.kennel.backend.entity.VetVisit;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VetVisitDtoMapper {
    private final AppointmentRepository appointmentRepository;

    public VetVisitResponseDto toDto(VetVisit vetVisit){
        return VetVisitResponseDto.builder()
                .id(vetVisit.getId())
                .notes(vetVisit.getNotes())
                .prescription(vetVisit.getPrescription())
                .slug(vetVisit.getSlug())
                .visitDate(vetVisit.getVisitDate())
                .owner(vetVisit.getOwner())
                .dog(vetVisit.getDog())
                .appointment(vetVisit.getAppointment())
                .clinic(vetVisit.getClinic())
                .doctor(vetVisit.getDoctor())
                .build();
    }

    public List<VetVisitResponseDto> toDto(List<VetVisit> vetVisits){
        return  vetVisits.stream()
                .map(this::toDto)
                .toList();
    }

    public VetVisit toEntity(VetVisitRequestDto vetVisitRequestDto){
        Appointment appointment= appointmentRepository.findById(vetVisitRequestDto.getAppointmentId())
                .orElseThrow(()-> new EntityNotFoundException(Appointment.class, "id", vetVisitRequestDto.getAppointmentId()));

        return VetVisit.builder()
                .notes(vetVisitRequestDto.getNotes())
                .prescription(vetVisitRequestDto.getPrescription())
                .visitDate(vetVisitRequestDto.getVisitDate())
                .appointment(appointment)
                .build();
    }
}
