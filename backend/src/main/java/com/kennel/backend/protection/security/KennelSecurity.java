package com.kennel.backend.protection.security;

import com.kennel.backend.repository.KennelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("kennelSecurity")
@RequiredArgsConstructor
public class KennelSecurity {
    private final KennelRepository kennelRepository;

    public Boolean isOwner(String slug, String email){
        return kennelRepository.findBySlug(slug)
                .map(kennel->kennel.getOwner().getEmail().equals(email))
                .orElse(false);
    }
}
