package com.kennel.backend.dto.follow.response;

import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import lombok.Builder;

@Builder
public class FollowResponseDto {
    private UserDetailsResponseDto follower;
    private UserDetailsResponseDto following;
}
