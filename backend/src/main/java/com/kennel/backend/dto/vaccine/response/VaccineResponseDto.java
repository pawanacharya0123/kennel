package com.kennel.backend.dto.vaccine.response;

import jakarta.persistence.Column;
import lombok.Builder;

@Builder
public class VaccineResponseDto {
    private String name;
    private String description;
    private String slug;
}
