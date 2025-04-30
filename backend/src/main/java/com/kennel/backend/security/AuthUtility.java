package com.kennel.backend.security;

import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtility {
    private final UserEntityRepository userEntityRepository;

    public  UserEntity getCurrentUser() {
        String authUserEmail = getCurrentUserEmail();

        return userEntityRepository.findByEmail(authUserEmail)
                .orElseThrow(()-> new EntityNotFoundException(UserEntity .class, "email",authUserEmail));
    }

    public String getCurrentUserEmail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
