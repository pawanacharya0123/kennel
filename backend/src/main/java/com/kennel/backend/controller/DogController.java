package com.kennel.backend.controller;

import com.kennel.backend.dto.dog.request.DogCreateRequestDTO;
import com.kennel.backend.dto.dog.request.DogUpdateRequestDTO;
import com.kennel.backend.dto.dog.response.DogResponseDTO;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dog")
@RequiredArgsConstructor
public class DogController {
    private DogService dogService;

    @PostMapping("/{ownerId}")
    public ResponseEntity<DogResponseDTO>  createDog(@RequestBody DogCreateRequestDTO dogCreateRequestDTO, @PathVariable Long ownerId){
        return ResponseEntity.ok(dogService.createDog(dogCreateRequestDTO, ownerId));
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
    public ResponseEntity<List<DogResponseDTO>> getDogsByUser(@PathVariable Long userId){
        return ResponseEntity.ok(dogService.getDogsByUser(userId));
    }

    @GetMapping("/kennel/{kennelSlug}")
    public ResponseEntity<List<DogResponseDTO>> getDogsByKannel(@PathVariable String kennelSlug){
        return ResponseEntity.ok(dogService.getDogsByKennel(kennelSlug));
    }

    @PatchMapping("/{slug}/for-sale")
    public ResponseEntity<DogResponseDTO> setDogForSale(@PathVariable String slug, @RequestParam Float price){
        return ResponseEntity.ok(dogService.setDogForSale(slug, price));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<DogResponseDTO> updateDog(@PathVariable String slug, @RequestBody DogUpdateRequestDTO dogUpdateRequestDTO){
        return ResponseEntity.ok(dogService.updateDog(slug, dogUpdateRequestDTO));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteDog(@PathVariable String slug){
        dogService.deleteDog(slug);
        return ResponseEntity.noContent().build();
    }

}
