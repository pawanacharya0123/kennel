package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.dog.DogDtoMapper;
import com.kennel.backend.dto.dog.request.DogCreateRequestDTO;
import com.kennel.backend.dto.dog.request.DogUpdateRequestDTO;
import com.kennel.backend.dto.dog.response.DogResponseDTO;
import com.kennel.backend.entity.Comment;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.Kennel;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.ForbiddenActionException;
import com.kennel.backend.protection.customAnnotation.EnableSoftDeleteFilter;
import com.kennel.backend.repository.DogRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.service.DogService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {

    private final DogRepository dogRepository;
    private final UserEntityRepository userEntityRepository;
    private final DogDtoMapper dogDtoMapper;
    private final SlugGenerator slugGenerator;

    @Override
    public DogResponseDTO createDog(DogCreateRequestDTO dogCreateRequestDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user= userEntityRepository.findByEmail(userEmail)
                .orElseThrow(()-> new EntityNotFoundException(UserEntity.class, "email", userEmail));

        Dog dog= dogDtoMapper.toEntity(dogCreateRequestDTO);

        validateDogNameUnique(dog.getName(), user.getId());

//        String initialSlug = SlugGenerator.toSlug(dog.getName() + "-" + dog.getBreed());
//        String finalSlug = ensureUniqueDogSlug(initialSlug);

        String base= dog.getName() + "-" + dog.getBreed();
        String finalSlug = slugGenerator.ensureUniqueSlug(base, dogRepository);

        dog.setSlug(finalSlug);
        dog.setOwner(user);

        return dogDtoMapper.toDto(dogRepository.save(dog));
    }

    @Override
    @EnableSoftDeleteFilter
    public DogResponseDTO getDogById(Long id) {
        Dog dog= dogRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "id", id));
        return dogDtoMapper.toDto(dog);
    }

    @Override
    @EnableSoftDeleteFilter
    public Page<DogResponseDTO> getDogsByUser(Long userId, Pageable pageable) {
        Page<Dog> dogs= dogRepository.findByOwnerId(userId, pageable);
        return dogDtoMapper.toDto(dogs);
    }

    @Override
    @EnableSoftDeleteFilter
    public Page<DogResponseDTO> getDogsByKennel(String kennelSlug, Pageable pageable) {
        Page<Dog> dogs= dogRepository.findByKennelSlug(kennelSlug, pageable);
        return dogDtoMapper.toDto(dogs);
    }

    @Override
    @EnableSoftDeleteFilter
    public DogResponseDTO setDogForSale(String slug, Float price) {
        Dog dog= dogRepository.findBySlug(slug)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", slug));

        checkAccess(dog);

        dog.setIsForSale(true);
        dog.setPrice(price);
        Dog updatedDog= dogRepository.save(dog);
        return dogDtoMapper.toDto(updatedDog);
    }

    @Override
    @EnableSoftDeleteFilter
    public DogResponseDTO updateDog(String slug, DogUpdateRequestDTO dogUpdateRequestDTO) {
        Dog dog = dogRepository.findBySlug(slug)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", slug));

        checkAccess(dog);

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
    @EnableSoftDeleteFilter
    public void deleteDog(String slug) {
        Dog dog = dogRepository.findBySlug(slug)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", slug));

        checkAccess(dog);

        dogRepository.delete(dog);
    }

    @Override
    @EnableSoftDeleteFilter
    public DogResponseDTO getDogBySlug(String slug) {
        Dog dog= dogRepository.findBySlug(slug)
                .orElseThrow(()-> new EntityNotFoundException(Dog.class, "slug", slug));
        return dogDtoMapper.toDto(dog);
    }

    private void checkAccess(Dog dog){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!dog.getOwner().getEmail().equals(userEmail)){
            throw new ForbiddenActionException(Comment.class);
        }
    }

    private void validateDogNameUnique(String name, Long ownerId){
        boolean exists = dogRepository.existsByNameAndOwnerId(name, ownerId);
        if (exists) {
            throw new IllegalStateException("Dog with name '" + name + "' already exists for this owner.");
        }
    }

//    private String ensureUniqueDogSlug(String baseSlug) {
//        String slug = baseSlug;
//        int counter = 1;
//        while (dogRepository.existsBySlug(slug)) {
//            slug = baseSlug + "-" + counter;
//            counter++;
//        }
//        return slug;
//    }
}
