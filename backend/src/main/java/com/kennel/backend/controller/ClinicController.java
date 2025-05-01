package com.kennel.backend.controller;

import com.kennel.backend.dto.clinic.request.ClinicRequestDto;
import com.kennel.backend.dto.clinic.response.ClinicResponseDto;
import com.kennel.backend.entity.Clinic;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clinics")
public class ClinicController {
    private final ClinicService clinicService;

    @GetMapping
    public ResponseEntity<List<ClinicResponseDto>> getAll(){
        return ResponseEntity.ok(clinicService.getAll());
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
    public ResponseEntity<Set<UserEntity>> getDoctors(@PathVariable String slug){
        return ResponseEntity.ok(clinicService.getDoctors(slug));
    }

    @GetMapping("/{slug}/manager")
    public ResponseEntity<UserEntity> getManager(@PathVariable String slug){
        return ResponseEntity.ok(clinicService.getManager(slug));
    }
}
