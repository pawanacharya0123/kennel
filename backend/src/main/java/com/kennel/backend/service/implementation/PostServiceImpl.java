package com.kennel.backend.service.implementation;

import com.kennel.backend.dto.post.PostDtoMapper;
import com.kennel.backend.dto.post.request.PostRequestDto;
import com.kennel.backend.dto.post.response.PostResponseDto;
import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.exception.EntityNotFoundException;
import com.kennel.backend.repository.PostRepository;
import com.kennel.backend.repository.UserEntityRepository;
import com.kennel.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserEntityRepository userEntityRepository;
    private final PostDtoMapper postDtoMapper;

    @Override
    public List<PostResponseDto> getAllPost() {
        return postDtoMapper.toDto(postRepository.findAll());
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "id", id));
        return postDtoMapper.toDto(post);
    }

    @Override
    public PostResponseDto getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "slug", slug));
        return postDtoMapper.toDto(post);
    }

    @Override
    public void deletePost(String slug) {
        postRepository.deleteBySlug(slug);
    }

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long userId) {
        UserEntity userEntity = userEntityRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, "id", userId));

        Post post = postDtoMapper.toEntity(postRequestDto);

        post.setCreatedBy(userEntity);

        return postDtoMapper.toDto(postRepository.save(post));
    }

    @Override
    public PostResponseDto updatePost(String slug, PostRequestDto postRequestDto) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(Post.class, "slug", slug));

        Post postRequest = postDtoMapper.toEntity(postRequestDto);

        Post updatePost = post.toBuilder()
                .content(postRequest.getContent())
                .build();

        return postDtoMapper.toDto(postRepository.save(post));
    }

    @Override
    public List<PostResponseDto> getPostsByUser(Long userId) {
        List<Post> posts = postRepository.findByCreatedById(userId);
        return postDtoMapper.toDto(posts);
    }
}
