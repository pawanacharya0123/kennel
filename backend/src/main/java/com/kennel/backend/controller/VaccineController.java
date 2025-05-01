package com.kennel.backend.controller;

import com.kennel.backend.dto.vaccine.request.VaccineRequestDto;
import com.kennel.backend.dto.vaccine.response.VaccineResponseDto;
import com.kennel.backend.entity.Vaccine;
import com.kennel.backend.service.VaccineService;
import com.kennel.backend.service.implementation.VaccineServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vaccines")
public class VaccineController {
    private final VaccineService vaccineService;

    @GetMapping
    public ResponseEntity<List<VaccineResponseDto>> getAll(){
        return ResponseEntity.ok(vaccineService.getAll());
    }

    @PostMapping
    public ResponseEntity<VaccineResponseDto> create(@RequestBody VaccineRequestDto vaccineRequestDto){
        return ResponseEntity.ok(vaccineService.create(vaccineRequestDto));
    }
}
