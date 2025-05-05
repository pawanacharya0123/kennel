package com.kennel.backend.dto.userEntity;

import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.Role;
import com.kennel.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserEntityDtoMapper {

    public UserDetailsResponseDto toDto(UserEntity user){
        return UserDetailsResponseDto.builder()
                .email(user.getEmail())
                .extraInfo(user.getExtraInfo())
                .roles(user.getRoles().stream().map(role -> role.getRoleName().toString()).collect(Collectors.toSet()))
                .build();
    }

    public List<UserDetailsResponseDto> toDto(List<UserEntity> users){
        return users.stream().map(this::toDto).toList();
    }

    public Page<UserDetailsResponseDto> toDto(Page<UserEntity> users){
        return users.map(this::toDto);
    }
}
