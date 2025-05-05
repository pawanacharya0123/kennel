package com.kennel.backend.dto.friend.request;

import com.kennel.backend.entity.enums.FriendStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendRequestDto {
    @NotNull
    @NotBlank
    private FriendStatus status;
    @NotNull
    private Long receiver;
}
