package com.kennel.backend.controller;

import com.kennel.backend.dto.vaccineRecord.response.VaccineRecordResponseDto;
import com.kennel.backend.dto.vetVisit.response.VetVisitResponseDto;
import com.kennel.backend.service.VaccineRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vaccine-records")
//@PreAuthorize("hasRole('ADMIN')")
public class VaccineRecordController {
    private final VaccineRecordService vaccineRecordService;

    @GetMapping("/dog/{dogSlug}")
    public ResponseEntity<Page<VaccineRecordResponseDto>> getVaccinationRecordByDog(
            String dogSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(vaccineRecordService.getVaccinationRecordByDog(dogSlug, pageable));
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<Page<VaccineRecordResponseDto>> getVaccinationRecordByDoctor(
            Long doctorId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(vaccineRecordService.getVaccinationRecordByDoctor(doctorId, pageable));
    }

    @GetMapping("/vet-visit/{vetVisitId}")
    public ResponseEntity<Page<VaccineRecordResponseDto>> getVaccinationRecordByVetVisit(
            Long vetVisitId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(vaccineRecordService.getVaccinationRecordByVetVisit(vetVisitId, pageable));
    }

    @GetMapping("/clinic/{clinicSlug}")
    public ResponseEntity<Page<VaccineRecordResponseDto>> getVaccinationRecordByClinic(
            String clinicSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(vaccineRecordService.getVaccinationRecordByClinic(clinicSlug, pageable));
    }

    @GetMapping("/vaccine/{vaccineSlug}")
    public ResponseEntity<Page<VaccineRecordResponseDto>> getVaccinationRecordByVaccine(
            String vaccineSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(vaccineRecordService.getVaccinationRecordByVaccine(vaccineSlug, pageable));
    }

    @PatchMapping("/vet-visit/{vetVisitId}")
    public ResponseEntity<VaccineRecordResponseDto> vaccinate(@PathVariable Long vetVisitId, @RequestParam String vaccineSlug){
        return ResponseEntity.ok(vaccineRecordService.vaccinate(vetVisitId,vaccineSlug));
    }
}
