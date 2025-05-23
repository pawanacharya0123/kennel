package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.appointments.AppointmentDtoMapper;
import com.kennel.backend.dto.appointments.request.AppointmentRequestDto;
import com.kennel.backend.dto.appointments.response.AppointmentResponseDto;
import com.kennel.backend.entity.Appointment;
import com.kennel.backend.entity.Clinic;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.AppointmentStatus;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.UnauthorizedAccessException;
import com.kennel.backend.repository.AppointmentRepository;
import com.kennel.backend.repository.DogRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.AppointmentService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AuthUtility authUtility;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDtoMapper appointmentDtoMapper;
    private final DogRepository dogRepository;
    private final SlugGenerator slugGenerator;

    @Override
    public Page<AppointmentResponseDto> getAll(Pageable pageable) {
        UserEntity currentAuthUser= authUtility.getCurrentUser();
        return appointmentDtoMapper.toDto(appointmentRepository.findByOwner(currentAuthUser, pageable));
    }

    @Override
    public Page<AppointmentResponseDto> getAllAppointmentsByDog(String dogSlug, Pageable pageable) {
        UserEntity currentAuthUser= authUtility.getCurrentUser();

        Dog dog = dogRepository.findBySlug(dogSlug)
                .orElseThrow(() -> new EntityNotFoundException(Dog.class, "slug", dogSlug));

        if(!dog.getOwner().getId().equals(currentAuthUser.getId())){
            throw new UnauthorizedAccessException(Dog.class, dogSlug);
        }

        return appointmentDtoMapper.toDto(appointmentRepository.findByDog(dog, pageable));
    }

    @Override
    public AppointmentResponseDto makeAppointment(AppointmentRequestDto appointmentRequestDto) {
        Appointment appointment = appointmentDtoMapper.toEntity(appointmentRequestDto);

//        String initialSlug = SlugGenerator.toSlug(appointment.getDog().getName() + "-" + appointment.getAppointmentTime().toString());
//        String finalSlug = ensureUniqueAppointmentSlug(initialSlug);

        String base= appointment.getDog().getName() + "-" + appointment.getAppointmentTime().toString();
        String finalSlug= slugGenerator.ensureUniqueSlug(base, appointmentRepository);

        appointment.setSlug(finalSlug);

        return appointmentDtoMapper.toDto(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponseDto updateAppointmentTime(Long id, Date date) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Appointment.class, "id", id));

        UserEntity currentAuthUser= authUtility.getCurrentUser();

        if(!appointment.getOwner().getId().equals(currentAuthUser.getId())){
            throw new UnauthorizedAccessException(Appointment.class, id);
        }

        appointment.setAppointmentTime(date);
        return appointmentDtoMapper.toDto(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponseDto updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Appointment.class, "id", id));

        UserEntity currentAuthUser= authUtility.getCurrentUser();
        
        if(appointment.getStatus().equals(AppointmentStatus.BOOKED)
                && status.equals(AppointmentStatus.CANCELLED)
                && appointment.getOwner().getId().equals(currentAuthUser.getId())
        ){
            appointment.setStatus(status);

        } else if (appointment.getClinic().getManager().getId().equals(currentAuthUser.getId())) {
            appointment.setStatus(status);
        } else{
            throw new UnauthorizedAccessException(Appointment.class, id);
        }

        return appointmentDtoMapper.toDto(appointmentRepository.save(appointment));
    }


//    private String ensureUniqueAppointmentSlug(String baseSlug) {
//        String slug = baseSlug;
//        int counter = 1;
//        while (appointmentRepository.existsBySlug(slug)) {
//            slug = baseSlug + "-" + counter;
//            counter++;
//        }
//        return slug;
//    }


}
