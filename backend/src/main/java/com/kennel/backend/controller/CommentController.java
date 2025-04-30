package com.kennel.backend.controller;

import com.kennel.backend.dto.comment.request.CommentRequestDto;
import com.kennel.backend.dto.comment.response.CommentResponseDto;
import com.kennel.backend.entity.Comment;
import com.kennel.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postSlug}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable String postSlug){
        return ResponseEntity.ok(commentService.findCommentByPost(postSlug));
    }

    @PostMapping("post/{postSlug}")
    public ResponseEntity<CommentResponseDto> addCommentToPost(
            @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable String postSlug
    ){
        return ResponseEntity.ok(commentService.addCommentToPost(commentRequestDto, postSlug));
    }

    @PreAuthorize("@commentSecurity.isOwner(#slug, authentication.name)")
    @PutMapping("/slug/{slug}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable String slug
    ){
        return ResponseEntity.ok(commentService.updateComment(commentRequestDto, slug));
    }

    @PreAuthorize("@commentSecurity.isOwner(#slug, authentication.name)")
    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteComment(@PathVariable String slug){
        commentService.deleteComment(slug);
        return ResponseEntity.noContent().build();
    }

}
