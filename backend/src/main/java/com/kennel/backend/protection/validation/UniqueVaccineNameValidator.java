package com.kennel.backend.protection.validation;

import com.kennel.backend.protection.customAnnotation.UniqueUserEmail;
import com.kennel.backend.repository.VaccineRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueVaccineNameValidator implements ConstraintValidator<UniqueUserEmail, String> {
    private final VaccineRepository vaccineRepository;
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name != null && !vaccineRepository.existsByName(name);
    }
}
