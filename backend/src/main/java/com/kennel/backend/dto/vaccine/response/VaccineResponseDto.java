package com.kennel.backend.dto.vaccine.response;

import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import jakarta.persistence.Column;
import lombok.Builder;

@Builder
public class VaccineResponseDto {
    private String name;
    private String description;
    private String slug;
    private UserDetailsResponseDto creator;
}
