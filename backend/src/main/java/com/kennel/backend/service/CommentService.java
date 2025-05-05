package com.kennel.backend.service;

import com.kennel.backend.dto.comment.request.CommentRequestDto;
import com.kennel.backend.dto.comment.response.CommentResponseDto;
import com.kennel.backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    Page<CommentResponseDto> findCommentByPost(String postSlug, Pageable pageable);
    CommentResponseDto addCommentToPost(CommentRequestDto commentRequestDto, String postSlug);
    CommentResponseDto updateComment(CommentRequestDto commentRequestDto, String slug);
    void deleteComment(String slug);
}
