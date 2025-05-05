package com.kennel.backend.service;

import com.kennel.backend.dto.follow.request.FollowRequestDto;
import com.kennel.backend.dto.follow.response.FollowResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowerService {
    FollowResponseDto follow(FollowRequestDto followRequestDto);
    void unfollow(Long id);
    void unfollowByUser(Long userId);

    Page<FollowResponseDto> getAllFollowing(Pageable pageable);

    Page<FollowResponseDto> getAllFollowers(Pageable pageable);
}
