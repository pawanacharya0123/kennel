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

    @GetMapping("/posts/{postSlug}")
    public ResponseEntity<List<Reaction>> getReactionsFromPost(@PathVariable String postSlug){
        return ResponseEntity.ok(reactionService.getReactionsFromPost(postSlug));
    }

    @GetMapping("/comments/{commentSlug}")
    public ResponseEntity<List<Reaction>> getReactionsFromComment(@PathVariable String commentSlug){
        return ResponseEntity.ok(reactionService.getReactionsFromComment(commentSlug));
    }

//    @PostMapping
//    public ResponseEntity<Reaction> createReaction(@RequestBody Reaction reaction ){
//        return ResponseEntity.ok(reactionService.createReaction(reaction));
//    }

    @PostMapping("/posts/{postSlug}")
    public ResponseEntity<ReactionResponseDto> reactToPost(@PathVariable String postSlug, @RequestBody ReactionRequestDto reactionRequestDto){
        return ResponseEntity.ok(reactionService.reactToPost(postSlug, reactionRequestDto));
    }

    @PostMapping("/comments/{commentSlug}")
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
