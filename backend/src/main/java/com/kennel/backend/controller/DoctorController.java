package com.kennel.backend.controller;

import com.kennel.backend.dto.doctor.response.DoctorResponseDto;
import com.kennel.backend.service.DoctorService;
import com.kennel.backend.service.implementation.DoctorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<Page<DoctorResponseDto>> findAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(doctorService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<DoctorResponseDto>> findAll(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(doctorService.getById(id, pageable));
    }


    @GetMapping("/clinic/{clinicSlug}")
    public ResponseEntity<Page<DoctorResponseDto>> findByClinic(
            @PathVariable String clinicSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(doctorService.findByClinic(clinicSlug,pageable));
    }
}
