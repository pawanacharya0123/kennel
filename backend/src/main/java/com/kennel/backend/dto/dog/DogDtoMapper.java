package com.kennel.backend.dto.dog;

import com.kennel.backend.dto.dog.request.DogRequestDTO;
import com.kennel.backend.dto.dog.response.DogResponseDTO;
import com.kennel.backend.dto.kennel.KennelDtoMapper;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.entity.Dog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DogDtoMapper {
    private final UserEntityDtoMapper userEntityDtoMapper;
    private final KennelDtoMapper kennelDtoMapper;

    public Dog toEntity(DogRequestDTO dogCreateRequestDTO){
        return Dog.builder()
                .name(dogCreateRequestDTO.getName())
                .breed(dogCreateRequestDTO.getBreed())
                .description(dogCreateRequestDTO.getDescription())
                .dob(dogCreateRequestDTO.getDob())
                .build();
    }
    public DogResponseDTO toDto(Dog dog){
        return DogResponseDTO.builder()
                .name(dog.getName())
                .breed(dog.getBreed())
                .description(dog.getDescription())
                .dob(dog.getDob())
                .slug(dog.getSlug())
                .price(dog.getPrice())
                .isForSale(dog.getIsForSale())
                .owner(userEntityDtoMapper.toDto(dog.getOwner()))
                .kennel(kennelDtoMapper.toDto(dog.getKennel()))
                .build();
    }

    public List<DogResponseDTO> toDto(List<Dog> dogs){
        return dogs.stream().map(this::toDto).toList();
    }

    public Page<DogResponseDTO> toDto(Page<Dog> dogs){
        return dogs.map(this::toDto);
    }

}
