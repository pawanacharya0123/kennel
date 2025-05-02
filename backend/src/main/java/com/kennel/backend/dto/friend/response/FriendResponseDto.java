package com.kennel.backend.dto.friend.response;

import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.FriendStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class FriendResponseDto {
    private FriendStatus status;
    @NotNull
    private UserDetailsResponseDto from;
    @NotNull
    private UserDetailsResponseDto to;
}
