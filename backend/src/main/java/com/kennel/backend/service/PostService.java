package com.kennel.backend.service;

import com.kennel.backend.dto.post.request.PostRequestDto;
import com.kennel.backend.dto.post.response.PostResponseDto;
import com.kennel.backend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Page<PostResponseDto> getAllPost(Pageable pageable);
    PostResponseDto getPostById(Long id);
    PostResponseDto getPostBySlug(String slug);
    void deletePost(String slug);
    PostResponseDto createPost(PostRequestDto postRequestDto);
    PostResponseDto updatePost(String slug, PostRequestDto postRequestDto);
    Page<PostResponseDto> getPostsByUser(Long userId, Pageable pageable);
}
