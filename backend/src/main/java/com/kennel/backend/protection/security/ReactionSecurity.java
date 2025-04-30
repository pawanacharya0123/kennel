package com.kennel.backend.protection.security;

import com.kennel.backend.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("reactionSecurity")
@RequiredArgsConstructor
public class ReactionSecurity {
    private final ReactionRepository reactionRepository;

    public Boolean isOwner(Long reactionId, String email){
        return reactionRepository.findById(reactionId)
                .map(reaction -> reaction.getCreatedBy().getEmail().equals(email))
                .orElse(false);
    }
}
