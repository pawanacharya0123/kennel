package com.kennel.backend.dto.vetVisit;

import com.kennel.backend.dto.appointments.AppointmentDtoMapper;
import com.kennel.backend.dto.clinic.ClinicDtoMapper;
import com.kennel.backend.dto.doctor.DoctorDtoMapper;
import com.kennel.backend.dto.dog.DogDtoMapper;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.dto.vetVisit.request.VetVisitRequestDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;
import com.kennel.backend.entity.Appointment;
import com.kennel.backend.entity.VetVisit;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VetVisitDtoMapper {
    private final AppointmentRepository appointmentRepository;
    private final UserEntityDtoMapper userEntityDtoMapper;
    private final DogDtoMapper dogDtoMapper;
    private final AppointmentDtoMapper appointmentDtoMapper;
    private final ClinicDtoMapper clinicDtoMapper;
    private final DoctorDtoMapper doctorDtoMapper;

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

    public VetVisitResponseDto toDto(VetVisit vetVisit){
        return VetVisitResponseDto.builder()
                .id(vetVisit.getId())
                .notes(vetVisit.getNotes())
                .prescription(vetVisit.getPrescription())
                .slug(vetVisit.getSlug())
                .visitDate(vetVisit.getVisitDate())
                .owner(userEntityDtoMapper.toDto(vetVisit.getOwner()))
                .dog(dogDtoMapper.toDto(vetVisit.getDog()))
                .appointment(appointmentDtoMapper.toDto(vetVisit.getAppointment()))
                .clinic(clinicDtoMapper.toDto(vetVisit.getClinic()))
                .doctor(doctorDtoMapper.toDto(vetVisit.getDoctor()))
                .build();
    }

    public List<VetVisitResponseDto> toDto(List<VetVisit> vetVisits){
        return  vetVisits.stream()
                .map(this::toDto)
                .toList();
    }

    public Page<VetVisitResponseDto> toDto(Page<VetVisit> vetVisits){
        return vetVisits.map(this::toDto);
    }
}
