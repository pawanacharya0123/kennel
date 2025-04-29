package com.kennel.backend.dto.kennel.response;

import lombok.Builder;

import java.util.Date;

@Builder
public class KennelResponseDto {
    private String slug;
    private String name;
    private String location;
    private Date establishedAt;
}
