package com.kennel.backend.service;

import com.kennel.backend.dto.vaccine.request.VaccineRequestDto;
import com.kennel.backend.dto.vaccine.response.VaccineResponseDto;
import com.kennel.backend.entity.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VaccineService {
    Page<VaccineResponseDto> getAll(Pageable pageable);

    VaccineResponseDto create(VaccineRequestDto vaccineRequestDto);

    VaccineResponseDto update(String slug, VaccineRequestDto vaccineRequestDto);
}
