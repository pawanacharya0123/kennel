package com.kennel.backend.service;

import com.kennel.backend.dto.reaction.request.ReactionRequestDto;
import com.kennel.backend.dto.reaction.response.ReactionResponseDto;
import com.kennel.backend.entity.Reaction;
import com.kennel.backend.entity.enums.ReactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReactionService {
    Page<ReactionResponseDto> getReactionsFromPost(String postSlug, Pageable pageable);
    Page<ReactionResponseDto> getReactionsFromComment(String commentSlug, Pageable pageable);

//    Reaction createReaction(Reaction reaction);

    ReactionResponseDto reactToPost(String postSlug, ReactionRequestDto reactionRequestDto);

    ReactionResponseDto reactToComment(String commentSlug, ReactionRequestDto reactionRequestDto);

    ReactionResponseDto updateReaction(String slug, ReactionType type);
    void deleteReaction(String slug);
}
