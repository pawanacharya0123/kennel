package com.kennel.backend.service;

import com.kennel.backend.dto.vaccine.request.VaccineRequestDto;
import com.kennel.backend.dto.vaccine.response.VaccineResponseDto;
import com.kennel.backend.entity.Vaccine;

import java.util.List;

public interface VaccineService {
    List<VaccineResponseDto> getAll();

    VaccineResponseDto create(VaccineRequestDto vaccineRequestDto);

    VaccineResponseDto update(String slug, VaccineRequestDto vaccineRequestDto);
}
