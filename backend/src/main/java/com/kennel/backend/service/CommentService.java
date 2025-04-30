package com.kennel.backend.service;

import com.kennel.backend.dto.comment.request.CommentRequestDto;
import com.kennel.backend.dto.comment.response.CommentResponseDto;
import com.kennel.backend.entity.Comment;

import java.util.List;

public interface CommentService {
    List<CommentResponseDto> findCommentByPost(String postSlug);
    CommentResponseDto addCommentToPost(CommentRequestDto commentRequestDto, String postSlug);
    CommentResponseDto updateComment(CommentRequestDto commentRequestDto, String slug);
    void deleteComment(String slug);
}
