package com.kennel.backend.dto.vaccine.request;

import com.kennel.backend.protection.customAnnotation.UniqueVaccineName;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VaccineRequestDto {
    @NotBlank
    @UniqueVaccineName
    private String name;
    private String description;

}
