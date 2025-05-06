package com.kennel.backend.controller;

import com.kennel.backend.dto.comment.request.CommentRequestDto;
import com.kennel.backend.dto.comment.response.CommentResponseDto;
import com.kennel.backend.entity.Comment;
import com.kennel.backend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByPost(
            @PathVariable String postSlug,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
            ){
        return ResponseEntity.ok(commentService.findCommentByPost(postSlug, pageable));
    }

    @PostMapping("post/{postSlug}")
    public ResponseEntity<CommentResponseDto> addCommentToPost(
            @RequestBody @Valid CommentRequestDto commentRequestDto,
            @PathVariable String postSlug
    ){
        return ResponseEntity.ok(commentService.addCommentToPost(commentRequestDto, postSlug));
    }

    @PreAuthorize("@commentSecurity.isOwner(#slug, authentication.name)")
    @PutMapping("/{slug}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @RequestBody @Valid CommentRequestDto commentRequestDto,
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
