package com.kennel.backend.controller;

import com.kennel.backend.dto.vaccine.request.VaccineRequestDto;
import com.kennel.backend.dto.vaccine.response.VaccineResponseDto;
import com.kennel.backend.service.VaccineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/vaccines")
public class VaccineController {
    private final VaccineService vaccineService;

    @GetMapping
    public ResponseEntity<Page<VaccineResponseDto>> getAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(vaccineService.getAll(pageable));
    }

    @PostMapping
    public ResponseEntity<VaccineResponseDto> create(@RequestBody @Valid VaccineRequestDto vaccineRequestDto){
        return ResponseEntity.ok(vaccineService.create(vaccineRequestDto));
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<VaccineResponseDto> update(@PathVariable String slug, @RequestBody @Valid VaccineRequestDto vaccineRequestDto){
        return ResponseEntity.ok(vaccineService.update(slug, vaccineRequestDto));
    }


}
