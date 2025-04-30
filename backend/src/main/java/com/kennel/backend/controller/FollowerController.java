package com.kennel.backend.controller;

import com.kennel.backend.dto.follow.request.FollowRequestDto;
import com.kennel.backend.dto.follow.response.FollowResponseDto;
import com.kennel.backend.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;

    @PostMapping
    public ResponseEntity<FollowResponseDto> follow(@RequestBody FollowRequestDto followRequestDto){
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
    public ResponseEntity<List<FollowResponseDto>> getAllFollowers(){
        return ResponseEntity.ok(followerService.getAllFollowers());
    }

    @GetMapping("/following")
    public ResponseEntity<List<FollowResponseDto>> getAllFollowing(){
        return ResponseEntity.ok(followerService.getAllFollowing());
    }
}
