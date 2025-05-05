package com.kennel.backend.dto.follow.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FollowRequestDto {
    @NotNull
    private Long following;
}
