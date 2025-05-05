package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.reaction.ReactionMapper;
import com.kennel.backend.dto.reaction.request.ReactionRequestDto;
import com.kennel.backend.dto.reaction.response.ReactionResponseDto;
import com.kennel.backend.entity.Comment;
import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.Reaction;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.ReactionType;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.ForbiddenActionException;
import com.kennel.backend.repository.CommentRepository;
import com.kennel.backend.repository.PostRepository;
import com.kennel.backend.repository.ReactionRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final UserEntityRepository userEntityRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReactionMapper reactionMapper;
    private final AuthUtility authUtility;

    @Override
    public Page<ReactionResponseDto> getReactionsFromPost(String postSlug, Pageable pageable) {
        return reactionMapper.toDto(reactionRepository.findByPostSlug(postSlug, pageable));
    }

    @Override
    public Page<ReactionResponseDto> getReactionsFromComment(String commentSlug, Pageable pageable) {
        return reactionMapper.toDto(reactionRepository.findByCommentSlug(commentSlug, pageable));
    }

//    @Override
//    public Reaction createReaction(Reaction reaction) {
//        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserEntity userEntity = userEntityRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, "email", userEmail));
//
//        reaction.setCreatedBy(userEntity);
//
//        return reactionRepository.save(reaction);
//    }

    @Override
    public ReactionResponseDto reactToPost(String postSlug, ReactionRequestDto reactionRequestDto) {
        Post post = postRepository.findBySlug(postSlug)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "slug", postSlug));

        UserEntity currentAuthUser= authUtility.getCurrentUser();

        Reaction reaction = reactionMapper.toEntity(reactionRequestDto);
        reaction.setPost(post);
        post.getReactions().add(reaction);

        reaction.setCreatedBy(currentAuthUser);
        return reactionMapper.toDto(reactionRepository.save(reaction));
    }

    @Override
    public ReactionResponseDto reactToComment(String commentSlug, ReactionRequestDto reactionRequestDto) {
        Comment comment = commentRepository.findBySlug(commentSlug)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, "slug", commentSlug));

        UserEntity currentAuthUser= authUtility.getCurrentUser();

        Reaction reaction = reactionMapper.toEntity(reactionRequestDto);
        reaction.setComment(comment);
        comment.getReactions().add(reaction);

        reaction.setCreatedBy(currentAuthUser);
        return reactionMapper.toDto(reactionRepository.save(reaction));
    }

    @Override
    public ReactionResponseDto updateReaction(String slug, ReactionType type) {
        Reaction reaction = reactionRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Reaction.class, "slug", slug));

        checkAccess(reaction);

        reaction.setType(type);

        return reactionMapper.toDto(reactionRepository.save(reaction));
    }

    @Override
    public void deleteReaction(String slug) {
        Reaction reaction = reactionRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Reaction.class, "slug", slug));

        checkAccess(reaction);

        reactionRepository.delete(reaction);
    }

    private void checkAccess(Reaction reaction){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!reaction.getCreatedBy().getEmail().equals(userEmail)){
            throw new ForbiddenActionException(Comment.class);
        }
    }
}
