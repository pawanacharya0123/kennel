package com.kennel.backend.controller;

import com.kennel.backend.dto.friend.request.FriendRequestDto;
import com.kennel.backend.dto.friend.response.FriendResponseDto;
import com.kennel.backend.entity.enums.FriendStatus;
import com.kennel.backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<FriendResponseDto> sendFriendRequest(@RequestBody FriendRequestDto friendRequestDto){
        return ResponseEntity.ok(friendService.sendFriendRequest(friendRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FriendResponseDto> updateFriendRequest(@PathVariable Long id,  @RequestParam FriendStatus status){
        return ResponseEntity.ok(friendService.updateFriendRequest(id, status));
    }

    @GetMapping
    public ResponseEntity<List<FriendResponseDto>> getFriends(){
        return ResponseEntity.ok(friendService.getFriends());
    }

    @GetMapping("/req-sent")
    public ResponseEntity<List<FriendResponseDto>> getSentRequest(){
        return ResponseEntity.ok(friendService.getSentPendingRequests());
    }

    @GetMapping("/req-received")
    public ResponseEntity<List<FriendResponseDto>> getPendingRequests(){
        return ResponseEntity.ok(friendService.getReceivedPendingRequests());
    }


}

