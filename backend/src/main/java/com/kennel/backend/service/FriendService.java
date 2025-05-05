package com.kennel.backend.service;

import com.kennel.backend.dto.friend.request.FriendRequestDto;
import com.kennel.backend.dto.friend.response.FriendResponseDto;
import com.kennel.backend.entity.enums.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FriendService {
    FriendResponseDto sendFriendRequest(FriendRequestDto friendRequestDto);
    FriendResponseDto updateFriendRequest(Long id, FriendStatus status);
    Page<FriendResponseDto> getFriends(Pageable pageable);
    Page<FriendResponseDto> getSentPendingRequests(Pageable pageable);
    Page<FriendResponseDto> getReceivedPendingRequests(Pageable pageable);
}
