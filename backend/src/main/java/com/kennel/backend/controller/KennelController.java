package com.kennel.backend.controller;

import com.kennel.backend.dto.kennel.request.KennelCreateRequestDto;
import com.kennel.backend.dto.kennel.request.KennelRequestDto;
import com.kennel.backend.dto.kennel.request.KennelUpdateRequestDto;
import com.kennel.backend.dto.kennel.response.KennelResponseDto;
import com.kennel.backend.service.KennelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/kennels")
@RequiredArgsConstructor
public class KennelController {
    private final KennelService kennelService;

    @GetMapping("/{id}")
    public ResponseEntity<KennelResponseDto> getKennelById(@PathVariable Long id){
        return ResponseEntity.ok(kennelService.getKennelById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<KennelResponseDto> getKennelById(@PathVariable String slug){
        return ResponseEntity.ok(kennelService.getKennelBySlug(slug));
    }

    @GetMapping
    public ResponseEntity<Page<KennelResponseDto>> getAllKennels(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(kennelService.getAllKennels(pageable));
    }

    @GetMapping("/owner/{userId}")
    public ResponseEntity<Page<KennelResponseDto>> getKennelsByUser(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(kennelService.getKennelsByOwner(userId, pageable));
    }

    @PreAuthorize("@kennelSecurity.isOwner(#slug, authentication.name)")
    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteKennel(@PathVariable String slug){
        kennelService.deleteKennel(slug);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<KennelResponseDto> createKennel(@RequestBody @Valid KennelCreateRequestDto kennelCreateRequestDto){
        return ResponseEntity.ok(kennelService.createKennel(kennelCreateRequestDto));
    }

    @PreAuthorize("@kennelSecurity.isOwner(#slug, authentication.name)")
    @PutMapping("/{slug}")
    public ResponseEntity<KennelResponseDto> updateKennel(@RequestBody @Valid KennelUpdateRequestDto kennelUpdateRequestDto, @PathVariable String slug){
        return ResponseEntity.ok(kennelService.updateKennel(slug, kennelUpdateRequestDto));
    }

}
