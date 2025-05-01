package com.kennel.backend.controller;

import com.kennel.backend.dto.vetVisit.VetVisitDtoMapper;
import com.kennel.backend.dto.vetVisit.request.VetVisitRequestDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;
import com.kennel.backend.entity.VetVisit;
import com.kennel.backend.service.VetVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vet-visits")
public class VetVisitController {
    private final VetVisitService vetVisitService;

    @GetMapping
    public ResponseEntity<List<VetVisitResponseDto>> getAllVisits(){
        return ResponseEntity.ok(vetVisitService.getAllVisits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VetVisitResponseDto> getVisitById(@PathVariable Long id){
        return ResponseEntity.ok(vetVisitService.getVisitById(id));
    }

    @PostMapping
    public ResponseEntity<VetVisitResponseDto> createVetVisit(@RequestBody VetVisitRequestDto vetVisitRequestDto){
        return ResponseEntity.ok(vetVisitService.createVetVisit(vetVisitRequestDto));
    }

    @PatchMapping("/{id}/vaccine")
    public ResponseEntity<VetVisitResponseDto> vaccinate(@PathVariable Long id, @RequestParam String slug){
        return ResponseEntity.ok(vetVisitService.vaccinate(id,slug));
    }

}
