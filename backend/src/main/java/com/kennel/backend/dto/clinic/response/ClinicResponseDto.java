package com.kennel.backend.dto.clinic.response;

import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class ClinicResponseDto {
    private String name;
    private String address;
    private String slug;
    private UserDetailsResponseDto manager;
}
