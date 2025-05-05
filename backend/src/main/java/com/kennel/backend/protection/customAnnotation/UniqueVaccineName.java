package com.kennel.backend.protection.customAnnotation;

import com.kennel.backend.protection.validation.UniqueUserEmailValidator;
import com.kennel.backend.protection.validation.UniqueVaccineNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueVaccineNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueVaccineName {
    String message() default "Vaccine Name must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
