package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.comment.CommentDtoMapper;
import com.kennel.backend.dto.comment.request.CommentRequestDto;
import com.kennel.backend.dto.comment.response.CommentResponseDto;
import com.kennel.backend.entity.Comment;
import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.FriendStatus;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.ForbiddenActionException;
import com.kennel.backend.exception.UnauthorizedAccessException;
import com.kennel.backend.protection.customAnnotation.EnableSoftDeleteFilter;
import com.kennel.backend.repository.CommentRepository;
import com.kennel.backend.repository.FriendRepository;
import com.kennel.backend.repository.PostRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.CommentService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentDtoMapper commentDtoMapper;
    private final UserEntityRepository userEntityRepository;
    private final PostRepository postRepository;
    private final AuthUtility authUtility;
    private final FriendRepository friendRepository;
    
    @Override
    public Page<CommentResponseDto> findCommentByPost(String postSlug, Pageable pageable) {
        return commentDtoMapper
                .toDto(commentRepository.findByPostSlugAndDeletedFalse(postSlug, pageable));
    }

    @Override
    @EnableSoftDeleteFilter
    public CommentResponseDto addCommentToPost(CommentRequestDto commentRequestDto, String postSlug) {
        Post post = postRepository.findBySlugAndDeletedFalse(postSlug)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "slug", postSlug));

        UserEntity currentAuthUser = authUtility.getCurrentUser();

        //only comment on post you have access to
//        if  it's your own post
//        if it's your friend post
//        if it's someone you are following ***
        boolean access= post.getCreatedBy().getId().equals(currentAuthUser.getId()) || isAFriend(currentAuthUser, post.getCreatedBy());

        if(!access){
            throw new UnauthorizedAccessException(Post.class, currentAuthUser.getId());
        }

        Comment comment = commentDtoMapper.toEntity(commentRequestDto);
        comment.setPost(post);
        post.getComments().add(comment);

        String initialSlug = SlugGenerator.toSlug( comment.getContent().substring(0,10));
        String finalSlug = ensureUniqueDogSlug(initialSlug);

        comment.setSlug(finalSlug);
        comment.setCreatedBy(currentAuthUser);
        comment.setDeleted(false);

        return commentDtoMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, String slug) {
        Comment comment = commentRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, "slug", slug));

        checkAccess(comment);

        UserEntity currentAuthUser = authUtility.getCurrentUser();

        //check if it's your post or your friends
//        boolean access= comment.getPost().getCreatedBy().getId().equals(currentAuthUser.getId()) || isAFriend(currentAuthUser, comment.getPost().getCreatedBy());
//
//        if(!access){
//            throw new UnauthorizedAccessException(Post.class, currentAuthUser.getId());
//        }

        Comment commentRequest = commentDtoMapper.toEntity(commentRequestDto);

        comment.setContent(commentRequest.getContent());

        return commentDtoMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(String slug) {
        Comment comment = commentRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, "slug", slug));

        checkAccess(comment);
        comment.setDeleted(true);
        commentRepository.save(comment);
//        commentRepository.delete(comment);
    }

    private void checkAccess(Comment comment){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!comment.getCreatedBy().getEmail().equals(userEmail)){
            throw new ForbiddenActionException(Comment.class);
        }
    }

    private String ensureUniqueDogSlug(String baseSlug) {
        String slug = baseSlug;
        int counter = 1;
        while (postRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }

    private Boolean isAFriend(UserEntity currentAuthUser, UserEntity userEntity){
        return friendRepository.existsBySenderAndReceiverAndStatus(currentAuthUser, userEntity, FriendStatus.ACCEPTED)
                ||
                friendRepository.existsBySenderAndReceiverAndStatus(userEntity, currentAuthUser, FriendStatus.ACCEPTED);
    }
}
