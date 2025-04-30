package com.kennel.backend.dto.reaction;

import com.kennel.backend.dto.reaction.request.ReactionRequestDto;
import com.kennel.backend.dto.reaction.response.ReactionResponseDto;
import com.kennel.backend.entity.Reaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReactionMapper {
    public Reaction toEntity(ReactionRequestDto reactionRequestDto){
        return Reaction.builder()
                .type(reactionRequestDto.getType())
                .build();
    }

    public ReactionResponseDto toDto(Reaction reaction){
        return ReactionResponseDto.builder()
                .type(reaction.getType())
                .slug(reaction.getSlug())
                .user(reaction.getCreatedBy())
                .build();
    }

    public List<ReactionResponseDto> toDto(List<Reaction> reactions){
        return reactions.stream()
                .map(this::toDto)
                .toList();
    }
}
