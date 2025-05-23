package com.kennel.backend.controller;

import com.kennel.backend.dto.dog.request.DogCreateRequestDTO;
import com.kennel.backend.dto.dog.request.DogUpdateRequestDTO;
import com.kennel.backend.dto.dog.response.DogResponseDTO;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.service.DogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/dogs")
@RequiredArgsConstructor
public class DogController {
    private DogService dogService;

    @PostMapping
    public ResponseEntity<DogResponseDTO>  createDog(@RequestBody @Valid DogCreateRequestDTO dogCreateRequestDTO){
        return ResponseEntity.ok(dogService.createDog(dogCreateRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogResponseDTO> getDog(@PathVariable Long id){
        return ResponseEntity.ok(dogService.getDogById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<DogResponseDTO> getDog(@PathVariable String slug){
        return ResponseEntity.ok(dogService.getDogBySlug(slug));
    }

    @GetMapping("/owner/{userId}")
    public ResponseEntity<Page<DogResponseDTO>> getDogsByUser(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
            ){
        return ResponseEntity.ok(dogService.getDogsByUser(userId, pageable));
    }

    @GetMapping("/kennel/{kennelSlug}")
    public ResponseEntity<Page<DogResponseDTO>> getDogsByKannel(
            @PathVariable String kennelSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){
        return ResponseEntity.ok(dogService.getDogsByKennel(kennelSlug, pageable));
    }

    @PreAuthorize("@dogSecurity.isOwner(#slug, authentication.name)")
    @PatchMapping("/{slug}/for-sale")
    public ResponseEntity<DogResponseDTO> setDogForSale(@PathVariable String slug, @RequestParam Float price){
        return ResponseEntity.ok(dogService.setDogForSale(slug, price));
    }

    @PreAuthorize("@dogSecurity.isOwner(#slug, authentication.name)")
    @PutMapping("/{slug}")
    public ResponseEntity<DogResponseDTO> updateDog(@PathVariable String slug, @RequestBody @Valid DogUpdateRequestDTO dogUpdateRequestDTO){
        return ResponseEntity.ok(dogService.updateDog(slug, dogUpdateRequestDTO));
    }

    @PreAuthorize("@dogSecurity.isOwner(#slug, authentication.name)")
    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteDog(@PathVariable String slug){
        dogService.deleteDog(slug);
        return ResponseEntity.noContent().build();
    }

    //get Vaccines by dog

}
