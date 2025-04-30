package com.kennel.backend.service;

import com.kennel.backend.dto.post.request.PostRequestDto;
import com.kennel.backend.dto.post.response.PostResponseDto;
import com.kennel.backend.entity.Post;

import java.util.List;

public interface PostService {
    List<PostResponseDto> getAllPost();
    PostResponseDto getPostById(Long id);
    PostResponseDto getPostBySlug(String slug);
    void deletePost(String slug);
    PostResponseDto createPost(PostRequestDto postRequestDto);
    PostResponseDto updatePost(String slug, PostRequestDto postRequestDto);
    List<PostResponseDto> getPostsByUser(Long userId);
}
