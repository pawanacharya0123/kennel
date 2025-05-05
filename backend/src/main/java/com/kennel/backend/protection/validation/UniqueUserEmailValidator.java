package com.kennel.backend.protection.validation;

import com.kennel.backend.protection.customAnnotation.UniqueUserEmail;
import com.kennel.backend.repository.UserEntityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {
    private final UserEntityRepository userEntityRepository;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email != null && !userEntityRepository.existsByEmail(email);
    }
}
