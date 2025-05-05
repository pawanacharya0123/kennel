package com.kennel.backend.service;

import com.kennel.backend.dto.dog.request.DogCreateRequestDTO;
import com.kennel.backend.dto.dog.request.DogUpdateRequestDTO;
import com.kennel.backend.dto.dog.response.DogResponseDTO;
import com.kennel.backend.entity.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DogService {
    DogResponseDTO createDog(DogCreateRequestDTO dog);

    DogResponseDTO getDogById(Long id);

    Page<DogResponseDTO> getDogsByUser(Long userId, Pageable pageable);

    Page<DogResponseDTO> getDogsByKennel(String kennelSlug, Pageable pageable);

    DogResponseDTO setDogForSale(String slug, Float price);

    DogResponseDTO updateDog(String slug, DogUpdateRequestDTO updatedDog);

    void deleteDog(String slug);

    DogResponseDTO getDogBySlug(String slug);
}
