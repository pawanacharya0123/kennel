package com.kennel.backend.service;

import com.kennel.backend.dto.post.PostDtoMapper;
import com.kennel.backend.dto.post.request.PostRequestDto;
import com.kennel.backend.dto.post.response.PostResponseDto;
import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.FriendStatus;
import com.kennel.backend.exception.ForbiddenActionException;
import com.kennel.backend.exception.UnauthorizedAccessException;
import com.kennel.backend.repository.*;
import com.kennel.backend.security.AuthUtility;
import com.kennel.backend.service.implementation.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock private UserEntityRepository userEntityRepository;
    @Mock private PostDtoMapper postDtoMapper;
    @Mock private AuthUtility authUtility;
    @Mock private FriendRepository friendRepository;
    @Mock private FollowerRepository followerRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void shouldReturnPostById_WhenAuthorized() {
        Long postId = 1L;
        UserEntity author = new UserEntity();
        author.setId(42L);

        Post post = new Post();
        post.setId(postId);
        post.setCreatedBy(author);

        UserEntity currentUser = new UserEntity();
        currentUser.setId(42L); // same as author

        PostResponseDto responseDto = PostResponseDto.builder()
                .slug("slug-post")
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(authUtility.getCurrentUser()).thenReturn(currentUser);
        when(postDtoMapper.toDto(post)).thenReturn(responseDto);

        PostResponseDto result = postService.getPostById(postId);

        assertEquals("slug-post", result.getSlug());
    }

    @Test
    void shouldThrowUnauthorizedAccess_WhenUserNotAllowed() {
        Long postId = 1L;

        UserEntity author = new UserEntity();
        author.setId(1L);

        Post post = new Post();
        post.setCreatedBy(author);

        UserEntity currentUser = new UserEntity(); currentUser.setId(2L);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(authUtility.getCurrentUser()).thenReturn(currentUser);
        when(followerRepository.existsByFollowerAndFollowing(currentUser, author)).thenReturn(false);
        when(friendRepository.existsBySenderAndReceiverAndStatus(currentUser, author, FriendStatus.ACCEPTED)).thenReturn(false);
        when(friendRepository.existsBySenderAndReceiverAndStatus(author, currentUser, FriendStatus.ACCEPTED)).thenReturn(false);

        assertThrows(UnauthorizedAccessException.class, () -> postService.getPostById(postId));
    }

    @Test
    void shouldCreatePostSuccessfully() {
        PostRequestDto requestDto = new PostRequestDto("Hello World");

        String email= "test@example.com";

        UserEntity user = new UserEntity();
        user.setEmail(email);

        Post post = new Post();
        post.setContent("Hello World");
        post.setSlug("hello-world");

        Post savedPost = new Post();
        savedPost.setSlug("hello-world");
        savedPost.setCreatedBy(user);

        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setSlug("hello-world");

        when(authUtility.getCurrentUser()).thenReturn(user);
        when(postDtoMapper.toEntity(any(PostRequestDto.class))).thenReturn(post);
        when(postRepository.existsBySlug(anyString())).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        when(postDtoMapper.toDto(savedPost)).thenReturn(responseDto);

        PostResponseDto result =postService.createPost(requestDto);

        assertEquals("hello-world", result.getSlug());
    }
    @Test
    void shouldUpdatePostSuccessfully(){
        String slug = "hello-world";
        PostRequestDto requestDto = new PostRequestDto("new Hello World");

        Post requestPost = Post.builder()
                .content("new Hello World")
                .build();

        String email = "test@example.com";

        UserEntity user = new UserEntity();
        user.setEmail(email);

        Post post = new Post();
        post.setContent("Hello World");
        post.setSlug("hello-world");
        post.setCreatedBy(user);

        Post updatedPost = new Post();
        updatedPost.setContent("new Hello World");

        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setContent("new Hello World");

        when(postRepository.findBySlug(slug)).thenReturn(Optional.of(post));
        when(authUtility.getCurrentUser()).thenReturn(user);
        when(postDtoMapper.toEntity(requestDto)).thenReturn(requestPost);
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);
        when(postDtoMapper.toDto(updatedPost)).thenReturn(responseDto);

        PostResponseDto result = postService.updatePost(slug, requestDto);

        assertEquals("new Hello World", result.getContent());
    }

    @Test
    void shouldDeletePostWhenUserIsOwner() {
        String slug = "my-post";

        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");

        Post post = new Post();
        post.setSlug(slug);
        post.setCreatedBy(user);

        when(authUtility.getCurrentUser()).thenReturn(user);
        when(postRepository.findBySlug(slug)).thenReturn(Optional.of(post));

        postService.deletePost(slug);

        verify(postRepository).delete(post);
    }

    @Test
    void shouldThrowForbiddenActionExceptionWhenNotTheOwner(){
        String slug = "my-post";

        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");

        UserEntity differentUser = new UserEntity();
        differentUser.setEmail("differentUser@example.com");

        Post post = new Post();
        post.setCreatedBy(differentUser);

        when(authUtility.getCurrentUser()).thenReturn(user);
        when(postRepository.findBySlug(slug)).thenReturn(Optional.of(post));

        assertThrows(ForbiddenActionException.class, () -> {
            postService.deletePost(slug);
        });
    }

    @Test
    void shouldReturnPagedPostsForUser() {
        Long userId = 1L;

        Pageable pageable = PageRequest.of(0, 2);

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("user1@x.com");

        Post post1 = new Post(); post1.setSlug("first-post");
        Post post2 = new Post(); post2.setSlug("second-post");

        Page<Post> postPage = new PageImpl<>(List.of(post1, post2), pageable, 2);

        PostResponseDto dto1 = new PostResponseDto("Hello World", "first-post", null);
        PostResponseDto dto2 = new PostResponseDto("Another Post", "second-post", null);
        Page<PostResponseDto> dtoPage = new PageImpl<>(List.of(dto1, dto2), pageable, 2);

        when(userEntityRepository.findById(userId)).thenReturn(Optional.of(user));
        when(authUtility.getCurrentUser()).thenReturn(user); // Assume access is allowed
        when(postRepository.findByCreatedBy(user, pageable)).thenReturn(postPage);
        when(postDtoMapper.toDto(postPage)).thenReturn(dtoPage);

        Page<PostResponseDto> result = postService.getPostsByUser(userId, pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("first-post", result.getContent().get(0).getSlug());
    }

}
