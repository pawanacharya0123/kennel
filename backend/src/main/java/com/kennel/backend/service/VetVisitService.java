package com.kennel.backend.service;

import com.kennel.backend.dto.vetVisit.request.VetVisitRequestDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VetVisitService {
    Page<VetVisitResponseDto> getAllVisits(Pageable pageable);

    Page<VetVisitResponseDto> getAllVisitsByDog(String dogSlug, Pageable pageable);

    VetVisitResponseDto createVetVisit(VetVisitRequestDto vetVisitRequestDto);

    VetVisitResponseDto getVisitById(Long id);

//    VetVisitResponseDto vaccinate(Long id, String slug);
}
