package com.kennel.backend.dto.comment.response;

import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CommentResponseDto {
    public String slug;
    public String content;
    public UserDetailsResponseDto user;
}
