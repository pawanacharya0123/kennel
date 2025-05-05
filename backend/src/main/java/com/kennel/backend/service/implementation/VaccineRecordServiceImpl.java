package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.vaccine.VaccineDtoMapper;
import com.kennel.backend.dto.vaccineRecord.VaccineRecordDtoMapper;
import com.kennel.backend.dto.vaccineRecord.response.VaccineRecordResponseDto;
import com.kennel.backend.entity.*;
import com.kennel.backend.entity.enums.RoleName;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.UnauthorizedAccessException;
import com.kennel.backend.repository.*;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.VaccineRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class VaccineRecordServiceImpl implements VaccineRecordService {
    private final VaccineRecordRepository vaccineRecordRepository;
    private final VaccineRecordDtoMapper vaccineRecordDtoMapper;
    private final DogRepository dogRepository;
    private final UserEntityRepository userEntityRepository;
    private final VetVisitRepository vetVisitRepository;
    private final ClinicRepository clinicRepository;
    private final VaccineRepository vaccineRepository;
    private final AuthUtility authUtility;

    @Override
    public Page<VaccineRecordResponseDto> getVaccinationRecordByDog(String dogSlug, Pageable pageable) {
        Dog dog= dogRepository.findBySlug(dogSlug)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", dogSlug));

        return vaccineRecordDtoMapper.toDto(vaccineRecordRepository.findByDog(dog, pageable));
    }

    @Override
    public Page<VaccineRecordResponseDto> getVaccinationRecordByDoctor(Long doctorId, Pageable pageable) {
        UserEntity doctor = userEntityRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, "id", doctorId));

        if(doctor.getRoles().stream().noneMatch(role-> role.getRoleName().equals(RoleName.ROLE_DOCTOR))){
            throw new UnauthorizedAccessException(VaccineRecord.class, doctor.getId());
        }

        return vaccineRecordDtoMapper.toDto(vaccineRecordRepository.findByDoctor(doctor, pageable));
    }

    @Override
    public Page<VaccineRecordResponseDto> getVaccinationRecordByVetVisit(Long vetVisitId, Pageable pageable) {
        VetVisit vetVisit = vetVisitRepository.findById(vetVisitId)
                .orElseThrow(() -> new EntityNotFoundException(VetVisit.class, "id", vetVisitId));

        return vaccineRecordDtoMapper.toDto(vaccineRecordRepository.findByVetVisit(vetVisit, pageable));
    }

    @Override
    public Page<VaccineRecordResponseDto> getVaccinationRecordByClinic(String clinicSlug, Pageable pageable) {
        Clinic clinic = clinicRepository.findBySlug(clinicSlug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", clinicSlug));

        return vaccineRecordDtoMapper.toDto(vaccineRecordRepository.findByClinic(clinicSlug, pageable));
    }

    @Override
    public Page<VaccineRecordResponseDto> getVaccinationRecordByVaccine(String vaccineSlug, Pageable pageable) {
        Vaccine vaccine = vaccineRepository.findBySlug(vaccineSlug)
                .orElseThrow(() -> new EntityNotFoundException(Vaccine.class, "slug", vaccineSlug));

        return vaccineRecordDtoMapper.toDto(vaccineRecordRepository.findByVaccine(vaccineSlug, pageable));
    }

    @Override
    public VaccineRecordResponseDto vaccinate(Long vetVisitId, String vaccineSlug) {
        UserEntity currentAuthUser = authUtility.getCurrentUser();

        VetVisit visit = vetVisitRepository.findById(vetVisitId)
                .orElseThrow(() -> new EntityNotFoundException(VetVisit.class, "id", vetVisitId));

        if(!visit.getDoctor().getId().equals(currentAuthUser.getId())
                && !visit.getClinic().getManager().getId().equals(currentAuthUser.getId())
        ){
            throw new UnauthorizedAccessException(VetVisit.class, currentAuthUser.getId());
        }

        Vaccine vaccine= vaccineRepository.findBySlug(vaccineSlug)
                .orElseThrow(() -> new EntityNotFoundException(Vaccine.class, "slug", vaccineSlug));

        VaccineRecord vaccineRecord= VaccineRecord.builder()
                .vaccine(vaccine)
                .dog(visit.getDog())
                .vetVisit(visit)
                .clinic(visit.getClinic())
                .doctor(visit.getDoctor())
                .vaccinationDate(new Date())
                .build();
        return vaccineRecordDtoMapper.toDto(vaccineRecordRepository.save(vaccineRecord));
    }
}
