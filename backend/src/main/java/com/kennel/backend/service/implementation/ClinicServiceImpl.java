package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.clinic.ClinicDtoMapper;
import com.kennel.backend.dto.clinic.request.ClinicRequestDto;
import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.entity.Clinic;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.UnauthorizedAccessException;
import com.kennel.backend.repository.ClinicRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;
    private final ClinicDtoMapper clinicDtoMapper;
    private final AuthUtility authUtility;
    private final UserEntityRepository userEntityRepository;

    @Override
    public List<ClinicResponseDto> getAll() {
        return clinicDtoMapper.toDto(clinicRepository.findAll());
    }

    @Override
    public ClinicResponseDto getClinicBySlug(String slug) {
        Clinic clinic = clinicRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", slug));
        return clinicDtoMapper.toDto(clinic);
    }

    @Override
    public ClinicResponseDto createClinic(ClinicRequestDto clinicRequestDto) {
        UserEntity currentAuthUser =authUtility.getCurrentUser();

        Clinic clinic= clinicDtoMapper.toEntity(clinicRequestDto);
        clinic.setManager(currentAuthUser);

        return clinicDtoMapper.toDto(clinicRepository.save(clinic));
    }
    
    public ClinicResponseDto updateClinic(String slug, ClinicRequestDto clinicRequestDto){
        Clinic clinic = clinicRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", slug));

        Clinic clinicRequest = clinicDtoMapper.toEntity(clinicRequestDto);

        UserEntity currentAuthUser =authUtility.getCurrentUser();

        if(!clinic.getManager().getId().equals(currentAuthUser.getId())){
            throw new UnauthorizedAccessException(Clinic.class, currentAuthUser.getId());
        }

        clinic.setName(clinicRequest.getName());
        clinic.setAddress(clinicRequest.getAddress());

        return clinicDtoMapper.toDto(clinicRepository.save(clinic));
    }

    @Override
    public Set<UserEntity> getDoctors(String slug) {
        Clinic clinic = clinicRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", slug));
        return clinic.getDoctors();
    }

    @Override
    public UserEntity getManager(String slug) {
        Clinic clinic = clinicRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", slug));
        return clinic.getManager();
    }

    @Override
    public ClinicResponseDto changeManager(String slug, Long userId){
        Clinic clinic = clinicRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", slug));

        UserEntity currentAuthUser =authUtility.getCurrentUser();
        if(!clinic.getManager().getId().equals(currentAuthUser.getId())){
            throw new UnauthorizedAccessException(Clinic.class, currentAuthUser.getId());
        }

        UserEntity userEntity = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, "email", userId));

        clinic.setManager(userEntity);
        return clinicDtoMapper.toDto(clinicRepository.save(clinic));
    }

    
}
