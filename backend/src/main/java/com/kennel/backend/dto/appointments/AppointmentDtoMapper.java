package com.kennel.backend.dto.appointments;

import com.kennel.backend.dto.appointments.request.AppointmentRequestDto;
import com.kennel.backend.dto.appointments.response.AppointmentResponseDto;
import com.kennel.backend.dto.clinic.ClinicDtoMapper;
import com.kennel.backend.dto.doctor.DoctorDtoMapper;
import com.kennel.backend.dto.dog.DogDtoMapper;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.entity.Appointment;
import com.kennel.backend.entity.Clinic;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.ClinicRepository;
import com.kennel.backend.repository.DogRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentDtoMapper {
    private final DogRepository dogRepository;
    private final ClinicRepository clinicRepository;
    private final UserEntityRepository userEntityRepository;
    private final AuthUtility authUtility;
    private final UserEntityDtoMapper userEntityDtoMapper;
    private final DoctorDtoMapper doctorDtoMapper;
    private final DogDtoMapper dogDtoMapper;
    private final ClinicDtoMapper clinicDtoMapper;

    public Appointment toEntity(AppointmentRequestDto appointmentRequestDto){
        Dog dog= dogRepository.findBySlug(appointmentRequestDto.getDog())
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", appointmentRequestDto.getDog()));
        Clinic clinic= clinicRepository.findBySlug(appointmentRequestDto.getClinic())
                .orElseThrow(()-> new EntityNotFoundException(Clinic.class, "slug", appointmentRequestDto.getClinic()));
        UserEntity doctor= userEntityRepository.findById(appointmentRequestDto.getDoctor())
                .orElseThrow(()-> new EntityNotFoundException(UserEntity.class, "id", appointmentRequestDto.getDoctor()));
        UserEntity currentAuthUser= authUtility.getCurrentUser();

        return Appointment.builder()
                .appointmentTime(appointmentRequestDto.getAppointmentTime())
                .dog(dog)
                .owner(currentAuthUser)
                .clinic(clinic)
                .doctor(doctor)
                .note(appointmentRequestDto.getNote())
                .build();
    }

    public AppointmentResponseDto toDto(Appointment appointment){
        return AppointmentResponseDto.builder()
                .slug(appointment.getSlug())
                .status(appointment.getStatus())
                .appointmentTime(appointment.getAppointmentTime())
                .dog(dogDtoMapper.toDto(appointment.getDog()))
                .doctor(doctorDtoMapper.toDto(appointment.getDoctor()))
                .owner(userEntityDtoMapper.toDto(appointment.getOwner()))
                .clinic(clinicDtoMapper.toDto(appointment.getClinic()))
                .note(appointment.getNote())
                .build();
    }
    public List<AppointmentResponseDto> toDto(List<Appointment> appointments){
        return appointments.stream()
                .map(this::toDto)
                .toList();
    }

    public Page<AppointmentResponseDto> toDto(Page<Appointment> appointments){
        return appointments.map(this::toDto);
    }
}
