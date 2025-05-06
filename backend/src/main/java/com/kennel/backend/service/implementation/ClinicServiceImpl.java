package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.clinic.ClinicDtoMapper;
import com.kennel.backend.dto.clinic.request.ClinicRequestDto;
import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.dto.doctor.DoctorDtoMapper;
import com.kennel.backend.dto.doctor.response.DoctorResponseDto;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.Clinic;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.UnauthorizedAccessException;
import com.kennel.backend.protection.customAnnotation.EnableSoftDeleteFilter;
import com.kennel.backend.repository.ClinicRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.ClinicService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UserEntityDtoMapper userEntityDtoMapper;
    private final DoctorDtoMapper doctorDtoMapper;
    private final SlugGenerator slugGenerator;

    @Override
    @EnableSoftDeleteFilter
    public Page<ClinicResponseDto> getAll(Pageable pageable) {
        return clinicDtoMapper.toDto(
                clinicRepository.findAll(pageable)
//                        .findByDeletedFalse(pageable)
        );
    }

    @Override
    @EnableSoftDeleteFilter
    public ClinicResponseDto getClinicBySlug(String slug) {
        Clinic clinic = clinicRepository.findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", slug));
        return clinicDtoMapper.toDto(clinic);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ClinicResponseDto createClinic(ClinicRequestDto clinicRequestDto) {
        UserEntity currentAuthUser =authUtility.getCurrentUser();

        Clinic clinic= clinicDtoMapper.toEntity(clinicRequestDto);

        validateClinicNameUnique(clinic.getName(), clinic.getAddress());

//        String initialSlug = SlugGenerator.toSlug(clinic.getName() + "-" + clinic.getAddress());
//        String finalSlug = ensureUniqueClinicSlug(initialSlug);

        String base= clinic.getName() + "-" + clinic.getAddress();
        String finalSlug =slugGenerator.ensureUniqueSlug(base, clinicRepository);

        clinic.setSlug(finalSlug);
        clinic.setManager(currentAuthUser);

        return clinicDtoMapper.toDto(clinicRepository.save(clinic));
    }

    @Override
    @EnableSoftDeleteFilter
    public ClinicResponseDto updateClinic(String slug, ClinicRequestDto clinicRequestDto){
        Clinic clinic = clinicRepository.findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", slug));

        Clinic clinicRequest = clinicDtoMapper.toEntity(clinicRequestDto);

        UserEntity currentAuthUser =authUtility.getCurrentUser();

        if(!clinic.getManager().getId().equals(currentAuthUser.getId())){
            throw new UnauthorizedAccessException(Clinic.class, currentAuthUser.getId());
        }

        clinic.setName(clinicRequest.getName());
        clinic.setAddress(clinicRequest.getAddress());

        validateClinicNameUnique(clinic.getName(), clinic.getAddress());

        return clinicDtoMapper.toDto(clinicRepository.save(clinic));
    }

    @Override
    @EnableSoftDeleteFilter
    public Page<DoctorResponseDto> getDoctors(String slug, Pageable pageable) {
        Clinic clinic = clinicRepository.findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", slug));

        return doctorDtoMapper.toDto(
            userEntityRepository.findByClinics(clinic, pageable)
        );
    }

    @Override
    @EnableSoftDeleteFilter
    public UserDetailsResponseDto getManager(String slug) {
        Clinic clinic = clinicRepository.findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Clinic.class, "slug", slug));
        return userEntityDtoMapper.toDto(clinic.getManager());
    }

    @Override
    @EnableSoftDeleteFilter
    public ClinicResponseDto changeManager(String slug, Long userId){
        Clinic clinic = clinicRepository.findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
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

    private void validateClinicNameUnique(String name, String address){
        boolean exists = clinicRepository.existsByNameAndAddress(name, address);
        if (exists) {
            throw new IllegalStateException("Clinic with name '" + name + "' already exists for this owner.");
        }
    }

//    @EnableSoftDeleteFilter
//    private String ensureUniqueClinicSlug(String baseSlug) {
//        String slug = baseSlug;
//        int counter = 1;
//        while (clinicRepository.existsBySlug(slug)) {
//            slug = baseSlug + "-" + counter;
//            counter++;
//        }
//        return slug;
//    }
}
