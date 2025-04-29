package com.kennel.backend.dto.post.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class PostResponseDto {
    private String content;
    private String slug;
}
