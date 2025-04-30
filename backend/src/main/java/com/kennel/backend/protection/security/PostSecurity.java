package com.kennel.backend.protection.security;

import com.kennel.backend.repository.DogRepository;
import com.kennel.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("postSecurity")
@RequiredArgsConstructor
public class PostSecurity {
    private final PostRepository postRepository;

    public Boolean isOwner(String slug, String email){
        return postRepository.findBySlug(slug)
                .map(post-> post.getCreatedBy().getEmail().equals(email))
                .orElse(false);
    }
}
