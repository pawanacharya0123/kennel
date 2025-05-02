package com.kennel.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kennel.backend.dto.comment.request.CommentRequestDto;
import com.kennel.backend.dto.comment.response.CommentResponseDto;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.security.JwtDecoder;
import com.kennel.backend.security.JwtIssuer;
import com.kennel.backend.security.JwtProperties;
import com.kennel.backend.security.JwtToPrincipalConvertor;
import com.kennel.backend.service.CommentService;
import com.kennel.backend.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentService commentService;

    @TestConfiguration
    @EnableConfigurationProperties(JwtProperties.class)
    static class MockConfig {
        @Bean
        public CommentService commentService() {
            return Mockito.mock(CommentService.class);
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
    void shouldGetAllCommentsByPost() throws Exception {
        List<CommentResponseDto> commentResponseDto= List.of(
                new CommentResponseDto("slug1", "Nice post!", UserDetailsResponseDto.builder()
                        .email("user1@x.com").build()),
                new CommentResponseDto("slug2", "Interesting read", UserDetailsResponseDto.builder()
                        .email("user2@x.com").build())
        );
        when(commentService.findCommentByPost(anyString()))
                .thenReturn(commentResponseDto);

        mockMvc.perform(get("/comments/post/slug1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].slug").value("slug1"));
    }

    @Test
    void shouldAddCommentToPost() throws Exception{
        CommentRequestDto request = new CommentRequestDto("Nice post!");
        CommentResponseDto commentResponse = new CommentResponseDto("slug1", "Nice post!", UserDetailsResponseDto.builder()
                .email("user1@x.com").build());

        when(commentService.addCommentToPost(any(CommentRequestDto.class), eq("slug1")))
                .thenReturn(commentResponse);

        mockMvc.perform(post("/comments/post/slug1")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("slug1"));
    }

    @Test
    void shouldUpdateComment() throws Exception{
        CommentRequestDto request = new CommentRequestDto("Updated post!");
        CommentResponseDto commentResponse = new CommentResponseDto("slug-edit", "Updated post!", UserDetailsResponseDto.builder()
                .email("user1@x.com").build());

        when(commentService.updateComment(any(CommentRequestDto.class), eq("slug-edit")))
                .thenReturn(commentResponse);

        mockMvc.perform(put("/comments/slug-edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("slug-edit"));
    }

    @Test
    void shouldDeleteComment() throws Exception{
        doNothing().when(commentService).deleteComment(anyString());

        mockMvc.perform(delete("/comments/slug1"))
                .andExpect(status().isNoContent());
    }


}
