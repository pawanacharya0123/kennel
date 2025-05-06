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
import com.kennel.backend.protection.customAnnotation.EnableSoftDeleteFilter;
import com.kennel.backend.repository.FollowerRepository;
import com.kennel.backend.repository.FriendRepository;
import com.kennel.backend.repository.PostRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.PostService;
import com.kennel.backend.utility.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserEntityRepository userEntityRepository;
    private final PostDtoMapper postDtoMapper;
    private final AuthUtility authUtility;
    private final FriendRepository friendRepository;
    private final FollowerRepository followerRepository;
    private final SlugGenerator slugGenerator;

    @Override
    @EnableSoftDeleteFilter
    public Page<PostResponseDto> getAllPost(Pageable pageable) {
        UserEntity currentAuthUser = authUtility.getCurrentUser();
        return postDtoMapper.toDto(postRepository.findByCreatedBy(currentAuthUser,pageable));
//        return postDtoMapper.toDto(postRepository.findByCreatedByAndDeletedFalse(currentAuthUser,pageable));
    }

    @Override
    @EnableSoftDeleteFilter
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
//                .findByIdAndDeletedFalse(id)
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
    @EnableSoftDeleteFilter
    public PostResponseDto getPostBySlug(String slug) {
        Post post = postRepository
                .findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
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
    @EnableSoftDeleteFilter
    public Page<PostResponseDto> getPostsByUser(Long userId, Pageable pageable) {
        UserEntity userEntity = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, "id", userId));

        UserEntity currentAuthUser = authUtility.getCurrentUser();

        boolean access = currentAuthUser.equals(userEntity)
                || isFollowing(currentAuthUser, userEntity)
                || isAFriend(currentAuthUser, userEntity);

        if(!access){
            throw new UnauthorizedAccessException(Post.class, currentAuthUser.getId());
        }

        Page<Post> posts = postRepository.findByCreatedBy(userEntity, pageable);
//                .findByCreatedByAndDeletedFalse(userEntity, pageable);
        return postDtoMapper.toDto(posts);
    }

    @Override
    @EnableSoftDeleteFilter
    public void deletePost(String slug) {
        Post post = postRepository
                .findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "slug", slug));

        checkAccess(post);
        post.setDeleted(true);
        postRepository.save(post);
//        postRepository.delete(post);
    }

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        UserEntity currentAuthUser = authUtility.getCurrentUser();

        Post post = postDtoMapper.toEntity(postRequestDto);
        post.setCreatedBy(currentAuthUser);

//        String initialSlug = SlugGenerator.toSlug( post.getContent().substring(0,10));
//        String finalSlug = ensureUniquePostSlug(initialSlug);

        String base= post.getContent().substring(0,10);
        String finalSlug =slugGenerator.ensureUniqueSlug(base, postRepository);

        post.setSlug(finalSlug);
        post.setDeleted(false);

        return postDtoMapper.toDto(postRepository.save(post));
    }

    @Override
    @EnableSoftDeleteFilter
    public PostResponseDto updatePost(String slug, PostRequestDto postRequestDto) {
        Post post = postRepository.findBySlug(slug)
//                .findBySlugAndDeletedFalse(slug)
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
        if(!post.getCreatedBy().equals(currentAuthUser)){
            throw new ForbiddenActionException(Comment.class);
        }
    }

//    private String ensureUniquePostSlug(String baseSlug) {
//        String slug = baseSlug;
//        int counter = 1;
//        while (postRepository.existsBySlug(slug)) {
//            slug = baseSlug + "-" + counter;
//            counter++;
//        }
//        return slug;
//    }

    private Boolean isFollowing(UserEntity currentAuthUser, UserEntity userEntity){
        return  followerRepository.existsByFollowerAndFollowing(currentAuthUser, userEntity);
    }

    private Boolean isAFriend(UserEntity currentAuthUser, UserEntity userEntity){
        return friendRepository.existsBySenderAndReceiverAndStatus(currentAuthUser, userEntity, FriendStatus.ACCEPTED)
                ||
                friendRepository.existsBySenderAndReceiverAndStatus(userEntity, currentAuthUser, FriendStatus.ACCEPTED);
    }
}
