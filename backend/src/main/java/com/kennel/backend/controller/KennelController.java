package com.kennel.backend.controller;

import com.kennel.backend.dto.kennel.request.KennelCreateRequestDto;
import com.kennel.backend.dto.kennel.request.KennelRequestDto;
import com.kennel.backend.dto.kennel.request.KennelUpdateRequestDto;
import com.kennel.backend.dto.kennel.response.KennelResponseDto;
import com.kennel.backend.entity.Kennel;
import com.kennel.backend.service.KennelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kennel")
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
    public ResponseEntity<List<KennelResponseDto>> getAllKennels(){
        return ResponseEntity.ok(kennelService.getAllKennels());
    }

    @GetMapping("/owner/{userId}")
    public ResponseEntity<List<KennelResponseDto>> getKennelsByUser(@PathVariable Long userId){
        return ResponseEntity.ok(kennelService.getKennelsByOwner(userId));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteKennel(@PathVariable String slug){
        kennelService.deleteKennel(slug);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ownerId}")
    public ResponseEntity<KennelResponseDto> createKennel(@RequestBody KennelCreateRequestDto kennelCreateRequestDto, @PathVariable Long ownerId){
        return ResponseEntity.ok(kennelService.createKennel(kennelCreateRequestDto, ownerId));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<KennelResponseDto> updateKennel(@RequestBody KennelUpdateRequestDto kennelUpdateRequestDto, @PathVariable String slug){
        return ResponseEntity.ok(kennelService.updateKennel(slug, kennelUpdateRequestDto));
    }

}
