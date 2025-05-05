package com.kennel.backend.controller;

import com.kennel.backend.dto.clinic.request.ClinicRequestDto;
import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.dto.doctor.response.DoctorResponseDto;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.Clinic;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clinics")
public class ClinicController {
    private final ClinicService clinicService;

    @GetMapping
    public ResponseEntity<Page<ClinicResponseDto>> getAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(clinicService.getAll(pageable));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ClinicResponseDto> getClinicBySlug(@PathVariable String slug){
        return ResponseEntity.ok(clinicService.getClinicBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<ClinicResponseDto> createClinic(@RequestBody ClinicRequestDto clinicRequestDto){
        return ResponseEntity.ok(clinicService.createClinic(clinicRequestDto));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<ClinicResponseDto> createClinic(@PathVariable String slug, @RequestBody ClinicRequestDto clinicRequestDto){
        return ResponseEntity.ok(clinicService.updateClinic(slug, clinicRequestDto));
    }

    @PatchMapping("/{slug}/user-id")
    public ResponseEntity<ClinicResponseDto> changeManager(@RequestBody String slug, @RequestParam Long userId){
        return ResponseEntity.ok(clinicService.changeManager(slug, userId));
    }

    @GetMapping("/{slug}/veterinarians")
    public ResponseEntity<Page<DoctorResponseDto>> getDoctors(
            @PathVariable String slug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(clinicService.getDoctors(slug, pageable));
    }

    @GetMapping("/{slug}/manager")
    public ResponseEntity<UserDetailsResponseDto> getManager(@PathVariable String slug){
        return ResponseEntity.ok(clinicService.getManager(slug));
    }
}
