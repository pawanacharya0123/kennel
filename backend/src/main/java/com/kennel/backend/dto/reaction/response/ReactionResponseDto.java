package com.kennel.backend.dto.reaction.response;

import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.ReactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReactionResponseDto {
//    private Long id;
    @Enumerated(EnumType.STRING)
    private ReactionType type;
    private String slug;
    private UserDetailsResponseDto user;
}
