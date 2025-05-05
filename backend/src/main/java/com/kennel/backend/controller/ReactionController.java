package com.kennel.backend.controller;

import com.kennel.backend.dto.reaction.request.ReactionRequestDto;
import com.kennel.backend.dto.reaction.response.ReactionResponseDto;
import com.kennel.backend.entity.Comment;
import com.kennel.backend.entity.Dog;
import com.kennel.backend.entity.Reaction;
import com.kennel.backend.entity.enums.ReactionType;
import com.kennel.backend.exception.ForbiddenActionException;
import com.kennel.backend.service.ReactionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reactions")
public class ReactionController {
    private final ReactionService reactionService;

    @GetMapping("/post/{postSlug}")
    public ResponseEntity<Page<ReactionResponseDto>> getReactionsFromPost(
            @PathVariable String postSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            ){
        return ResponseEntity.ok(reactionService.getReactionsFromPost(postSlug, pageable));
    }

    @GetMapping("/comment/{commentSlug}")
    public ResponseEntity<Page<ReactionResponseDto>> getReactionsFromComment(
            @PathVariable String commentSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
            ){
        return ResponseEntity.ok(reactionService.getReactionsFromComment(commentSlug, pageable));
    }


    @PostMapping("/post/{postSlug}")
    public ResponseEntity<ReactionResponseDto> reactToPost(@PathVariable String postSlug, @RequestBody ReactionRequestDto reactionRequestDto){
        return ResponseEntity.ok(reactionService.reactToPost(postSlug, reactionRequestDto));
    }

    @PostMapping("/comment/{commentSlug}")
    public ResponseEntity<ReactionResponseDto> reactToComment(@PathVariable String commentSlug, @RequestBody ReactionRequestDto reactionRequestDto){
        return ResponseEntity.ok(reactionService.reactToComment(commentSlug, reactionRequestDto));
    }

    @PreAuthorize("@reactionSecurity.isOwner(#slug, authentication.name)")
    @PatchMapping("/{slug}")
    public ResponseEntity<ReactionResponseDto> updateReaction(@PathVariable String slug, @RequestParam ReactionType type){
        return ResponseEntity.ok(reactionService.updateReaction(slug, type));
    }

    @PreAuthorize("@reactionSecurity.isOwner(#slug, authentication.name)")
    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteReaction(@PathVariable String slug){
        reactionService.deleteReaction(slug);
        return ResponseEntity.noContent().build();
    }


}
