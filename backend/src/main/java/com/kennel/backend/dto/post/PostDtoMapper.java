package com.kennel.backend.dto.post;

import com.kennel.backend.dto.post.request.PostRequestDto;
import com.kennel.backend.dto.post.response.PostResponseDto;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.entity.Post;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostDtoMapper {
    private final UserEntityDtoMapper userEntityDtoMapper;

    public Post toEntity(PostRequestDto postRequestDto){
        return Post.builder()
                .content(postRequestDto.getContent())
                .build();
    }

    public PostResponseDto toDto(Post post){
        return PostResponseDto.builder()
                .content(post.getContent())
                .slug(post.getSlug())
                .user(userEntityDtoMapper.toDto(post.getCreatedBy()))
                .build();
    }

    public List<PostResponseDto> toDto(List<Post> posts){
        return posts.stream()
                .map(this::toDto)
                .toList();
    }

}
