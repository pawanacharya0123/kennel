package com.kennel.backend.service;

import com.kennel.backend.dto.vetVisit.request.VetVisitRequestDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;

import java.util.List;

public interface VetVisitService {
    List<VetVisitResponseDto> getAllVisits();

    VetVisitResponseDto createVetVisit(VetVisitRequestDto vetVisitRequestDto);

    VetVisitResponseDto getVisitById(Long id);

    VetVisitResponseDto vaccinate(Long id, String slug);
}
