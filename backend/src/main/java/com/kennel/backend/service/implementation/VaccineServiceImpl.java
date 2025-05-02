package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.vaccine.VaccineDtoMapper;
import com.kennel.backend.dto.vaccine.request.VaccineRequestDto;
import com.kennel.backend.dto.vaccine.response.VaccineResponseDto;
import com.kennel.backend.entity.Friend;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.Vaccine;
import com.kennel.backend.entity.enums.RoleName;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.ForbiddenActionException;
import com.kennel.backend.repository.VaccineRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.VaccineService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VaccineServiceImpl implements VaccineService {
    private final VaccineRepository vaccineRepository;
    private final VaccineDtoMapper vaccineDtoMapper;
    private final AuthUtility authUtility;

    @Override
    public List<VaccineResponseDto> getAll() {
        return vaccineDtoMapper.toDto(vaccineRepository.findAll());
    }

    @Override
    public VaccineResponseDto create(VaccineRequestDto vaccineRequestDto) {
        Vaccine vaccine= vaccineDtoMapper.toEntity(vaccineRequestDto);

        UserEntity currentAuthUser =authUtility.getCurrentUser();
        if (currentAuthUser.getRoles().stream().noneMatch(role->role.getRoleName().equals(RoleName.ROLE_ADMIN))){
            throw new ForbiddenActionException(Vaccine.class);
        }

        String initialSlug = SlugGenerator.toSlug(vaccine.getName() + "-" + vaccine.getDescription().substring(0,5));
        String finalSlug = ensureUniqueDogSlug(initialSlug);

        vaccine.setSlug(finalSlug);
        vaccine.setVaccineCreator(currentAuthUser);

        return vaccineDtoMapper.toDto(vaccineRepository.save(vaccine));
    }

    @Override
    public VaccineResponseDto update(String slug, VaccineRequestDto vaccineRequestDto) {

        Vaccine vaccine= vaccineRepository.findBySlug(slug)
                .orElseThrow(()-> new EntityNotFoundException(Vaccine.class, "slug", slug));

        UserEntity currentAuthUser =authUtility.getCurrentUser();
        if (currentAuthUser.getRoles().stream().noneMatch(role->role.getRoleName().equals(RoleName.ROLE_ADMIN))
            || vaccine.getVaccineCreator().getId().equals(currentAuthUser.getId())
        ){
            throw new ForbiddenActionException(Vaccine.class);
        }

        vaccine.setName(vaccineRequestDto.getName());
        vaccine.setDescription(vaccineRequestDto.getDescription());

        return vaccineDtoMapper.toDto(vaccineRepository.save(vaccine));
    }

    private String ensureUniqueDogSlug(String baseSlug) {
        String slug = baseSlug;
        int counter = 1;
        while (vaccineRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
}
