package com.kennel.backend.dto.comment;

import com.kennel.backend.dto.comment.request.CommentRequestDto;
import com.kennel.backend.dto.comment.response.CommentResponseDto;
import com.kennel.backend.dto.userEntity.UserEntityDtoMapper;
import com.kennel.backend.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class CommentDtoMapper {
    private final UserEntityDtoMapper userEntityDtoMapper;

    public Comment toEntity(CommentRequestDto commentRequestDto){
        return Comment.builder()
                .content(commentRequestDto.getContent())
                .build();
    }
    public CommentResponseDto toDto(Comment comment){
        return CommentResponseDto.builder()
                .content(comment.getContent())
                .slug(comment.getSlug())
                .user(userEntityDtoMapper.toDto(comment.getCreatedBy()))
                .build();
    }
    public List<CommentResponseDto> toDto(List<Comment> comments){
        return comments.stream()
                .map(this::toDto)
                .toList();
    }
    public Page<CommentResponseDto> toDto(Page<Comment> comments){
        return comments.map(this::toDto);
    }

}
