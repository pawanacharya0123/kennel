package com.kennel.backend.protection.customAnnotation;

import com.kennel.backend.protection.validation.UniqueUserEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUserEmailValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserEmail {
    String message() default "User email must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
