package com.kennel.backend.dto.reaction.request;

import com.kennel.backend.entity.enums.ReactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class ReactionRequestDto {
    @Enumerated(EnumType.STRING)
    private ReactionType type;
}
