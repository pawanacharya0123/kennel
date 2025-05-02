package com.kennel.backend.dto.post.response;

import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostResponseDto {
    private String content;
    private String slug;
    private UserDetailsResponseDto user;
}
