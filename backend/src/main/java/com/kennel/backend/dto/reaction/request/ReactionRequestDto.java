package com.kennel.backend.dto.reaction.request;

import com.kennel.backend.entity.enums.ReactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionRequestDto {
    @NotBlank
    @Enumerated(EnumType.STRING)
    private ReactionType type;
}
