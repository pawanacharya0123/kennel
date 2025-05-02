package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.post.PostDtoMapper;
import com.kennel.backend.dto.post.request.PostRequestDto;
import com.kennel.backend.dto.post.response.PostResponseDto;
import com.kennel.backend.entity.Comment;
import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.FriendStatus;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.exception.ForbiddenActionException;
import com.kennel.backend.exception.UnauthorizedAccessException;
import com.kennel.backend.repository.FollowerRepository;
import com.kennel.backend.repository.FriendRepository;
import com.kennel.backend.repository.PostRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.PostService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserEntityRepository userEntityRepository;
    private final PostDtoMapper postDtoMapper;
    private final AuthUtility authUtility;
    private final FriendRepository friendRepository;
    private final FollowerRepository followerRepository;

    @Override
    public List<PostResponseDto> getAllPost() {
        UserEntity currentAuthUser = authUtility.getCurrentUser();

        return postDtoMapper.toDto(postRepository.findByCreatedBy(currentAuthUser));
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "id", id));

        UserEntity currentAuthUser = authUtility.getCurrentUser();

        boolean access = post.getCreatedBy().getId().equals(currentAuthUser.getId())
                ||
                isFollowing(currentAuthUser, post.getCreatedBy())
                ||
                isAFriend(currentAuthUser, post.getCreatedBy());

        if(!access){
            throw new UnauthorizedAccessException(Post.class, currentAuthUser.getId());
        }

        return postDtoMapper.toDto(post);
    }

    @Override
    public PostResponseDto getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "slug", slug));

        UserEntity currentAuthUser = authUtility.getCurrentUser();

        boolean access = post.getCreatedBy().getId().equals(currentAuthUser.getId())
                ||
                isFollowing(currentAuthUser, post.getCreatedBy())
                ||
                isAFriend(currentAuthUser, post.getCreatedBy());

        if(!access){
            throw new UnauthorizedAccessException(Post.class, currentAuthUser.getId());
        }

        return postDtoMapper.toDto(post);
    }

    @Override
    public List<PostResponseDto> getPostsByUser(Long userId) {
        UserEntity userEntity = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, "id", userId));

        UserEntity currentAuthUser = authUtility.getCurrentUser();

        boolean access = isFollowing(currentAuthUser, userEntity) || isAFriend(currentAuthUser, userEntity);

        if(!access){
            throw new UnauthorizedAccessException(Post.class, currentAuthUser.getId());
        }

        List<Post> posts = postRepository.findByCreatedById(userId);
        return postDtoMapper.toDto(posts);
    }

    @Override
    public void deletePost(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "slug", slug));

        checkAccess(post);

        postRepository.delete(post);
    }

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        UserEntity currentAuthUser = authUtility.getCurrentUser();

        Post post = postDtoMapper.toEntity(postRequestDto);
        post.setCreatedBy(currentAuthUser);

        String initialSlug = SlugGenerator.toSlug( post.getContent().substring(0,10));
        String finalSlug = ensureUniqueDogSlug(initialSlug);

        post.setSlug(finalSlug);

        return postDtoMapper.toDto(postRepository.save(post));
    }

    @Override
    public PostResponseDto updatePost(String slug, PostRequestDto postRequestDto) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "slug", slug));

        checkAccess(post);

        Post postRequest = postDtoMapper.toEntity(postRequestDto);

        post.toBuilder()
                .content(postRequest.getContent())
                .build();

        return postDtoMapper.toDto(postRepository.save(post));
    }


    private void checkAccess(Post post){
        UserEntity currentAuthUser = authUtility.getCurrentUser();
        if(!post.getCreatedBy().getEmail().equals(currentAuthUser.getEmail())){
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

    private Boolean isFollowing(UserEntity currentAuthUser, UserEntity userEntity){
        return  followerRepository.existsByFollowerAndFollowing(currentAuthUser, userEntity);
    }

    private Boolean isAFriend(UserEntity currentAuthUser, UserEntity userEntity){
        return
                friendRepository.existsBySenderAndReceiverAndStatus(currentAuthUser, userEntity, FriendStatus.ACCEPTED)
                        ||
                        friendRepository.existsBySenderAndReceiverAndStatus(userEntity, currentAuthUser, FriendStatus.ACCEPTED);
    }
}
