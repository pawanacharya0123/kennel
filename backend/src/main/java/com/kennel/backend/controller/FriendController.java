package com.kennel.backend.controller;

import com.kennel.backend.dto.friend.request.FriendRequestDto;
import com.kennel.backend.dto.friend.response.FriendResponseDto;
import com.kennel.backend.entity.enums.FriendStatus;
import com.kennel.backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<FriendResponseDto>> getFriends(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(friendService.getFriends(pageable));
    }

    @GetMapping("/req-sent")
    public ResponseEntity<Page<FriendResponseDto>> getSentRequest(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(friendService.getSentPendingRequests(pageable));
    }

    @GetMapping("/req-received")
    public ResponseEntity<Page<FriendResponseDto>> getPendingRequests(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(friendService.getReceivedPendingRequests(pageable));
    }


}

