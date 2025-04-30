package com.kennel.backend.service;

import com.kennel.backend.dto.friend.request.FriendRequestDto;
import com.kennel.backend.dto.friend.response.FriendResponseDto;
import com.kennel.backend.entity.enums.FriendStatus;

import java.util.List;

public interface FriendService {
    FriendResponseDto sendFriendRequest(FriendRequestDto friendRequestDto);
    FriendResponseDto updateFriendRequest(Long id, FriendStatus status);
    List<FriendResponseDto> getFriends();
    List<FriendResponseDto> getSentPendingRequests();
    List<FriendResponseDto> getReceivedPendingRequests();
}
