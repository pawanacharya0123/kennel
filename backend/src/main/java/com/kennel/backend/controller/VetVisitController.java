package com.kennel.backend.controller;

import com.kennel.backend.dto.vetVisit.VetVisitDtoMapper;
import com.kennel.backend.dto.vetVisit.request.VetVisitRequestDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;
import com.kennel.backend.entity.VetVisit;
import com.kennel.backend.service.VetVisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vet-visits")
public class VetVisitController {
    private final VetVisitService vetVisitService;

    @GetMapping
    public ResponseEntity<Page<VetVisitResponseDto>> getAllVisits(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(vetVisitService.getAllVisits(pageable));
    }

    @GetMapping("/dog/{dogSlug}")
    public ResponseEntity<Page<VetVisitResponseDto>> getAllVisitsByDog(
            @PathVariable String dogSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(vetVisitService.getAllVisitsByDog(dogSlug, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VetVisitResponseDto> getVisitById(@PathVariable Long id){
        return ResponseEntity.ok(vetVisitService.getVisitById(id));
    }

    @PostMapping
    public ResponseEntity<VetVisitResponseDto> createVetVisit(@RequestBody @Valid VetVisitRequestDto vetVisitRequestDto){
        return ResponseEntity.ok(vetVisitService.createVetVisit(vetVisitRequestDto));
    }

//    @PatchMapping("/{vaccineSlug}/vaccine")
//    public ResponseEntity<VetVisitResponseDto> vaccinate(@PathVariable Long id, @RequestParam String vaccineSlug){
//        return ResponseEntity.ok(vetVisitService.vaccinate(id,vaccineSlug));
//    }



}
