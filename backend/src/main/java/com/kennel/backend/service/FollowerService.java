package com.kennel.backend.service;

import com.kennel.backend.dto.follow.request.FollowRequestDto;
import com.kennel.backend.dto.follow.response.FollowResponseDto;

import java.util.List;

public interface FollowerService {
    FollowResponseDto follow(FollowRequestDto followRequestDto);
    void unfollow(Long id);
    void unfollowByUser(Long userId);

    List<FollowResponseDto> getAllFollowing();

    List<FollowResponseDto> getAllFollowers();
}
