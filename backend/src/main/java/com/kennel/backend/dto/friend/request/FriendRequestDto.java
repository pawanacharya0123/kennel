package com.kennel.backend.dto.friend.request;

import com.kennel.backend.entity.enums.FriendStatus;
import lombok.Getter;

@Getter
public class FriendRequestDto {
    private FriendStatus status;
    private Long receiver;
}
