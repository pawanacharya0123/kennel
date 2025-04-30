package com.kennel.backend.protection.security;

import com.kennel.backend.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("dogSecurity")
@RequiredArgsConstructor
public class DogSecurity {
    private final DogRepository dogRepository;

    public Boolean isOwner(String slug, String email){
        return dogRepository.findBySlug(slug)
                .map(dog-> dog.getOwner().getEmail().equals(email))
                .orElse(false);
    }
}
