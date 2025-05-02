package com.kennel.backend.dto.userEntity;

import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserEntityDtoMapper {

    public UserDetailsResponseDto toDto(UserEntity user){
        return UserDetailsResponseDto.builder()
                .email(user.getEmail())
                .extraInfo(user.getExtraInfo())
                .roles(user.getRoles())
                .build();
    }

    public List<UserDetailsResponseDto> toDto(List<UserEntity> users){
        return users.stream().map(this::toDto).toList();
    }
}
