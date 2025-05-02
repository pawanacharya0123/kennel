package com.kennel.backend.dto.dog.response;

import com.kennel.backend.dto.kennel.response.KennelResponseDto;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Builder
public class DogResponseDTO {
    private String slug;
    private String name;
    private String breed;
    private Date dob;
    private String description;
    private Boolean isForSale;
    private Float price;
    private UserDetailsResponseDto owner;
    private KennelResponseDto kennel;
}
