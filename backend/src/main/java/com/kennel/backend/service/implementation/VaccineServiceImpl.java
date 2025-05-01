package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.vaccine.VaccineDtoMapper;
import com.kennel.backend.dto.vaccine.request.VaccineRequestDto;
import com.kennel.backend.dto.vaccine.response.VaccineResponseDto;
import com.kennel.backend.entity.Vaccine;
import com.kennel.backend.repository.VaccineRepository;
import com.kennel.backend.service.VaccineService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccineServiceImpl implements VaccineService {
    private final VaccineRepository vaccineRepository;
    private final VaccineDtoMapper vaccineDtoMapper;
    @Override
    public List<VaccineResponseDto> getAll() {
        return vaccineDtoMapper.toDto(vaccineRepository.findAll());
    }

    @Override
    public VaccineResponseDto create(VaccineRequestDto vaccineRequestDto) {
        Vaccine vaccine= vaccineDtoMapper.toEntity(vaccineRequestDto);

        String initialSlug = SlugGenerator.toSlug(vaccine.getName() + "-" + vaccine.getDescription().substring(0,5));
        String finalSlug = ensureUniqueDogSlug(initialSlug);

        vaccine.setSlug(finalSlug);

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
