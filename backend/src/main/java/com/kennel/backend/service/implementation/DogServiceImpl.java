package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.dog.DogDtoMapper;
import com.kennel.backend.dto.dog.request.DogCreateRequestDTO;
import com.kennel.backend.dto.dog.request.DogUpdateRequestDTO;
import com.kennel.backend.dto.dog.response.DogResponseDTO;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.DogRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.service.DogService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {

    private final DogRepository dogRepository;
    private final UserEntityRepository userEntityRepository;
    private final DogDtoMapper dogDtoMapper;

    @Override
    public DogResponseDTO createDog(DogCreateRequestDTO dogCreateRequestDTO, Long ownerId) {
        UserEntity user= userEntityRepository.findById(ownerId)
                .orElseThrow(()-> new EntityNotFoundException(UserEntity.class, "id", ownerId));

        Dog dog= dogDtoMapper.toEntity(dogCreateRequestDTO);

        String initialSlug = SlugGenerator.toSlug(dog.getName() + "-" + dog.getBreed());
        String finalSlug = ensureUniqueDogSlug(initialSlug);

        dog.setSlug(finalSlug);
        dog.setOwner(user);

        validateDogNameUnique(dog.getName(), ownerId);

        return dogDtoMapper.toDto(dogRepository.save(dog));
    }

    @Override
    public DogResponseDTO getDogById(Long id) {
        Dog dog= dogRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "id", id));
        return dogDtoMapper.toDto(dog);
    }

    @Override
    public List<DogResponseDTO> getDogsByUser(Long userId) {
        List<Dog> dogs= dogRepository.findByOwnerId(userId);
        return dogDtoMapper.toDto(dogs);
    }

    @Override
    public List<DogResponseDTO> getDogsByKennel(String kennelSlug) {
        List<Dog> dogs= dogRepository.findByKennelSlug(kennelSlug);
        return dogDtoMapper.toDto(dogs);
    }

    @Override
    public DogResponseDTO setDogForSale(String slug, Float price) {
        Dog dog= dogRepository.findBySlug(slug)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", slug));

        dog.setIsForSale(true);
        dog.setPrice(price);
        Dog updatedDog= dogRepository.save(dog);
        return dogDtoMapper.toDto(updatedDog);
    }

    @Override
    public DogResponseDTO updateDog(String slug, DogUpdateRequestDTO dogUpdateRequestDTO) {
        Dog dog = dogRepository.findBySlug(slug)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", slug));

        Dog updatedDogRequest= dogDtoMapper.toEntity(dogUpdateRequestDTO);

        Dog updatedDog= dog.toBuilder()
                .name(updatedDogRequest.getName())
                .breed(updatedDogRequest.getBreed())
                .dob(updatedDogRequest.getDob())
                .description(updatedDogRequest.getDescription())
                .build();

        validateDogNameUnique(dog.getName(), dog.getOwner().getId());

        return dogDtoMapper.toDto(dogRepository.save(updatedDog));
    }

    @Override
    public void deleteDog(String slug) {
        Dog dog = dogRepository.findBySlug(slug)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", slug));

        dogRepository.delete(dog);
    }

    @Override
    public DogResponseDTO getDogBySlug(String slug) {
        Dog dog= dogRepository.findBySlug(slug)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", slug));
        return dogDtoMapper.toDto(dog);
    }

    private void validateDogNameUnique(String name, Long ownerId){
        boolean exists = dogRepository.existsByNameAndOwnerId(name, ownerId);
        if (exists) {
            throw new IllegalStateException("Dog with name '" + name + "' already exists for this owner.");
        }
    }

    private String ensureUniqueDogSlug(String baseSlug) {
        String slug = baseSlug;
        int counter = 1;
        while (dogRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
}
