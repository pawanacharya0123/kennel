package com.kennel.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kennel.backend.dto.post.request.PostRequestDto;
import com.kennel.backend.dto.post.response.PostResponseDto;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.security.JwtDecoder;
import com.kennel.backend.security.JwtIssuer;
import com.kennel.backend.security.JwtProperties;
import com.kennel.backend.security.JwtToPrincipalConvertor;
import com.kennel.backend.service.PostService;
import com.kennel.backend.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    @TestConfiguration
    @EnableConfigurationProperties(JwtProperties.class)
    static class MockConfig {
        @Bean
        public PostService postService() {
            return Mockito.mock(PostService.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }

        @Bean
        public JwtIssuer jwtIssuer(JwtProperties properties) {
            return new JwtIssuer(properties); // Uses the auto-configured JwtProperties
        }

        @Bean
        public JwtDecoder jwtDecoder(JwtProperties properties) {
            return new JwtDecoder(properties); // Uses the auto-configured JwtProperties
        }

        @Bean
        public JwtToPrincipalConvertor jwtToPrincipalConvertor() {
            return new JwtToPrincipalConvertor();
        }
    }

    @Test
    void shouldGetAllPosts() throws Exception {
        List<PostResponseDto> posts = List.of(
                new PostResponseDto( "Hello World", "first-post",  UserDetailsResponseDto.builder()
                        .email("user1@x.com").build()),
                new PostResponseDto("Another Post", "second-post", UserDetailsResponseDto.builder()
                        .email("user2@x.com").build())
        );
        Page<PostResponseDto> page = new PageImpl<>(posts);

        when(postService.getAllPost(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].slug").value("first-post"));
    }

    @Test
    void shouldGetPostById() throws Exception{
        PostResponseDto postResponseDto= new PostResponseDto( "Hello World", "first-post", UserDetailsResponseDto.builder()
                .email("user1@x.com").build());

        when(postService.getPostById(any())).thenReturn(postResponseDto);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("first-post"));
    }

    @Test
    void shouldGetPostBySlug() throws Exception{
        PostResponseDto postResponseDto= new PostResponseDto( "Hello World", "first-post",UserDetailsResponseDto.builder()
                .email("user1@x.com").build());

        when(postService.getPostBySlug(anyString())).thenReturn(postResponseDto);

        mockMvc.perform(get("/posts/slug/first-post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("first-post"));
    }

    @Test
    void shouldGetPostsByUser() throws Exception{
        List<PostResponseDto> posts = List.of(
                new PostResponseDto( "Hello World", "first-post",UserDetailsResponseDto.builder()
                        .email("user1@x.com").build()),
                new PostResponseDto("Another Post", "second-post",UserDetailsResponseDto.builder()
                        .email("user1@x.com").build())
        );
        Page<PostResponseDto> page = new PageImpl<>(posts);

        when(postService.getPostsByUser(anyLong(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/posts/creator/1")
                        .param("page", "0")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].slug").value("first-post"));
    }

    @Test
    void shouldDeletePost() throws Exception {
        doNothing().when(postService).deletePost("delete-me");

        mockMvc.perform(delete("/posts/delete-me"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCreatePost() throws Exception {
        PostRequestDto request = new PostRequestDto("New post!");
        PostResponseDto response = new PostResponseDto( "New post!","new-post", UserDetailsResponseDto.builder()
                .email("user1@x.com").build());

        when(postService.createPost(any(PostRequestDto.class))).thenReturn(response);
        System.out.println("Response: " + objectMapper.writeValueAsString(response));

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("New post!"));
    }

    @Test
    void shouldUpdatePost() throws Exception {
        PostRequestDto update = new PostRequestDto("Updated content");
        PostResponseDto response = new PostResponseDto( "Updated content", "my-post", UserDetailsResponseDto.builder()
                .email("user1@x.com").build());

        when(postService.updatePost(eq("my-post"), any(PostRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/posts/my-post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated content"));
    }

}
