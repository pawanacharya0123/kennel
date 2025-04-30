package com.kennel.backend.controller;

import com.kennel.backend.dto.post.request.PostRequestDto;
import com.kennel.backend.dto.post.response.PostResponseDto;
import com.kennel.backend.dto.reaction.request.ReactionRequestDto;
import com.kennel.backend.dto.reaction.response.ReactionResponseDto;
import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.Reaction;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPost());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<PostResponseDto> getPostBySlug(@PathVariable String slug){
        return ResponseEntity.ok(postService.getPostBySlug(slug));
    }

    @GetMapping("/creator/{userId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(@PathVariable Long userId){
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @PreAuthorize("@postSecurity.isOwner(#slug, authentication.name)")
    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteSlugBySlug(@PathVariable String slug){
        postService.deletePost(slug);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto){
        return ResponseEntity.ok(postService.createPost(postRequestDto));
    }

    @PreAuthorize("@postSecurity.isOwner(#slug, authentication.name)")
    @PutMapping("/{slug}")
    public ResponseEntity<PostResponseDto> updatePost(@RequestBody PostRequestDto postRequestDto, @PathVariable String slug){
        return ResponseEntity.ok(postService.updatePost(slug, postRequestDto));
    }




}
