package com.kennel.backend.dto.comment;

import com.kennel.backend.dto.comment.request.CommentRequestDto;
import com.kennel.backend.dto.comment.response.CommentResponseDto;
import com.kennel.backend.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CommentDtoMapper {
    public Comment toEntity(CommentRequestDto commentRequestDto){
        return Comment.builder()
                .content(commentRequestDto.getContent())
                .build();
    }
    public CommentResponseDto toDto(Comment comment){
        return CommentResponseDto.builder()
                .content(comment.getContent())
                .build();
    }
    public List<CommentResponseDto> toDto(List<Comment> comments){
        return comments.stream()
                .map(this::toDto)
                .toList();
    }

}
