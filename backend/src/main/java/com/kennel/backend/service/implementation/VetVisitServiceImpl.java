package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.vetVisit.VetVisitDtoMapper;
import com.kennel.backend.dto.vetVisit.request.VetVisitRequestDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;
import com.kennel.backend.entity.*;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.UnauthorizedAccessException;
import com.kennel.backend.repository.DogRepository;
import com.kennel.backend.repository.VaccineRepository;
import com.kennel.backend.repository.VetVisitRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.VetVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VetVisitServiceImpl implements VetVisitService {
    private final VetVisitRepository vetVisitRepository;
    private final AuthUtility authUtility;
    private final VetVisitDtoMapper vetVisitDtoMapper;
    private final VaccineRepository vaccineRepository;
    private final DogRepository dogRepository;

    @Override
    public Page<VetVisitResponseDto> getAllVisits(Pageable pageable) {
        UserEntity currentAuthUser = authUtility.getCurrentUser();
        return vetVisitDtoMapper.toDto(vetVisitRepository.findByOwner(currentAuthUser, pageable));
    }

    @Override
    public Page<VetVisitResponseDto> getAllVisitsByDog(String dogSlug, Pageable pageable) {
        UserEntity currentAuthUser = authUtility.getCurrentUser();
        Dog dog = dogRepository.findBySlug(dogSlug)
                .orElseThrow(() -> new EntityNotFoundException(Dog.class, "slug", dogSlug));

        if(!dog.getOwner().equals(currentAuthUser)){
            throw new UnauthorizedAccessException(VetVisit.class, currentAuthUser.getId());
        }
        return vetVisitDtoMapper.toDto(vetVisitRepository.findByDog(dog, pageable));
    }

    @Override
    public VetVisitResponseDto createVetVisit(VetVisitRequestDto vetVisitRequestDto) {
        VetVisit visit = vetVisitDtoMapper.toEntity(vetVisitRequestDto);

        UserEntity currentAuthUser = authUtility.getCurrentUser();

        Appointment appointment= visit.getAppointment();

        visit.setDog(appointment.getDog());
        visit.setOwner(appointment.getOwner());
        visit.setClinic(appointment.getClinic());
        visit.setDoctor(appointment.getDoctor());

        if(!visit.getDoctor().getId().equals(currentAuthUser.getId())
                && !visit.getClinic().getManager().getId().equals(currentAuthUser.getId())
        ){
            throw new UnauthorizedAccessException(VetVisit.class, currentAuthUser.getId());
        }

        return vetVisitDtoMapper.toDto(vetVisitRepository.save(visit));
    }

    @Override
    public VetVisitResponseDto getVisitById(Long id) {
        UserEntity currentAuthUser = authUtility.getCurrentUser();

        VetVisit visit = vetVisitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VetVisit.class, "id", id));

        if(!visit.getDoctor().getId().equals(currentAuthUser.getId())
                && !visit.getClinic().getManager().getId().equals(currentAuthUser.getId())
                && !visit.getOwner().getId().equals(currentAuthUser.getId())
        ){
            throw new UnauthorizedAccessException(VetVisit.class, currentAuthUser.getId());
        }

        return vetVisitDtoMapper.toDto(visit);
    }

}
