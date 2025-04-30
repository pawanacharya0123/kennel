package com.kennel.backend.dto.reaction.response;

import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.ReactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public class ReactionResponseDto {
    @Enumerated(EnumType.STRING)
    private ReactionType type;
    private String slug;
    private UserEntity user;
}
