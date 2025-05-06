package com.kennel.backend.controller;

import com.kennel.backend.dto.follow.request.FollowRequestDto;
import com.kennel.backend.dto.follow.response.FollowResponseDto;
import com.kennel.backend.service.FollowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;

    @PostMapping
    public ResponseEntity<FollowResponseDto> follow(@RequestBody @Valid FollowRequestDto followRequestDto){
        return ResponseEntity.ok(followerService.follow(followRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unfollow(@PathVariable Long id){
        followerService.unfollow(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/unfollow/{userId}")
    public ResponseEntity<Void> unfollowByUser(@PathVariable Long userId){
        followerService.unfollowByUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/followers")
    public ResponseEntity<Page<FollowResponseDto>> getAllFollowers(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(followerService.getAllFollowers(pageable));
    }

    @GetMapping("/following")
    public ResponseEntity<Page<FollowResponseDto>> getAllFollowing(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ){
        return ResponseEntity.ok(followerService.getAllFollowing(pageable));
    }
}
