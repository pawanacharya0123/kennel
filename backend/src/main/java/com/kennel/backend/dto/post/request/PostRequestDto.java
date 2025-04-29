package com.kennel.backend.dto.post.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostRequestDto {
    @NotNull
    private String content;
    private String slug;
}
