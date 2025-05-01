package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.vetVisit.VetVisitDtoMapper;
import com.kennel.backend.dto.vetVisit.request.VetVisitRequestDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;
import com.kennel.backend.entity.*;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.UnauthorizedAccessException;
import com.kennel.backend.repository.VaccineRepository;
import com.kennel.backend.repository.VetVisitRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.VetVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VetVisitServiceImpl implements VetVisitService {
    private final VetVisitRepository vetVisitRepository;
    private final AuthUtility authUtility;
    private final VetVisitDtoMapper vetVisitDtoMapper;
    private final VaccineRepository vaccineRepository;

    @Override
    public List<VetVisitResponseDto> getAllVisits() {
        UserEntity currentAuthUser = authUtility.getCurrentUser();
        return vetVisitDtoMapper.toDto(vetVisitRepository.findByOwner(currentAuthUser));
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

    @Override
    public VetVisitResponseDto vaccinate(Long id, String slug) {
        UserEntity currentAuthUser = authUtility.getCurrentUser();

        VetVisit visit = vetVisitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VetVisit.class, "id", id));

        if(!visit.getDoctor().getId().equals(currentAuthUser.getId())
                && !visit.getClinic().getManager().getId().equals(currentAuthUser.getId())
        ){
            throw new UnauthorizedAccessException(VetVisit.class, currentAuthUser.getId());
        }

        Vaccine vaccine= vaccineRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Vaccine.class, "slug", slug));

        VaccineRecord vaccineRecord= VaccineRecord.builder()
                .vaccine(vaccine)
                .dog(visit.getDog())
                .vetVisit(visit)
                .clinic(visit.getClinic())
                .doctor(visit.getDoctor())
                .vaccinationDate(new Date())
                .build();

//      VaccineRecordRepository.save(vaccineRecord);

        visit.getVaccines().add(vaccineRecord);

//        Set<VaccineRecord> vaccines = new HashSet<>(visit.getVaccines());
//        vaccines.add(vaccineRecord);
//
//        visit.setVaccines(vaccines);

        return vetVisitDtoMapper.toDto(visit);
    }
}
