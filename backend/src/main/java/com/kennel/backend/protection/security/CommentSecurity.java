package com.kennel.backend.protection.security;

import com.kennel.backend.repository.CommentRepository;
import com.kennel.backend.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("commentSecurity")
@RequiredArgsConstructor
public class CommentSecurity {
    private final CommentRepository commentRepository;

    public Boolean isOwner(String slug, String email){
        return commentRepository.findBySlug(slug)
                .map(comment-> comment.getCreatedBy().getEmail().equals(email))
                .orElse(false);
    }
}
