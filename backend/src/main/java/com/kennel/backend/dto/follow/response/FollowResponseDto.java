package com.kennel.backend.dto.follow.response;

import com.kennel.backend.entity.UserEntity;
import lombok.Builder;

@Builder
public class FollowResponseDto {
    private UserEntity follower;
    private UserEntity following;
}
