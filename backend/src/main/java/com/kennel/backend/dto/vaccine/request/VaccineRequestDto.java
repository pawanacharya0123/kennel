package com.kennel.backend.dto.vaccine.request;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class VaccineRequestDto {
    private String name;
    private String description;

}
