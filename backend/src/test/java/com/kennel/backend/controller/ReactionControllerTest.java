package com.kennel.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kennel.backend.dto.reaction.request.ReactionRequestDto;
import com.kennel.backend.dto.reaction.response.ReactionResponseDto;
import com.kennel.backend.dto.userEntity.response.UserDetailsResponseDto;
import com.kennel.backend.entity.Reaction;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.ReactionType;
import com.kennel.backend.security.JwtDecoder;
import com.kennel.backend.security.JwtIssuer;
import com.kennel.backend.security.JwtProperties;
import com.kennel.backend.security.JwtToPrincipalConvertor;
import com.kennel.backend.service.CommentService;
import com.kennel.backend.service.ReactionService;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReactionService reactionService;

    @TestConfiguration
    @EnableConfigurationProperties(JwtProperties.class)
    static class MockConfig {
        @Bean
        public ReactionService reactionService() {
            return Mockito.mock(ReactionService.class);
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
    void shouldGetReactionsFromPost() throws Exception{

        List<Reaction> reactions= List.of(
                Reaction.builder()
                        .type(ReactionType.LIKE)
                        .createdBy(new UserEntity("user1@x.com", "xyz"))
                        .build(),
                Reaction.builder()
                        .type(ReactionType.LIKE)
                        .createdBy(new UserEntity("user2@x.com", "abc"))
                        .build()
        );

        when(reactionService.getReactionsFromPost("post-slug-1")).thenReturn(reactions);

        mockMvc.perform(get("/reactions/post/post-slug-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void shouldGetReactionsFromComment() throws Exception{

        List<Reaction> reactions= List.of(
                Reaction.builder()
                        .type(ReactionType.LIKE)
                        .createdBy(new UserEntity("user1@x.com", "xyz"))
                        .build(),
                Reaction.builder()
                        .type(ReactionType.LIKE)
                        .createdBy(new UserEntity("user2@x.com", "abc"))
                        .build()
        );

        when(reactionService.getReactionsFromComment("comment-slug-1")).thenReturn(reactions);

        mockMvc.perform(get("/reactions/comment/comment-slug-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void shouldReactToPost()  throws Exception {
        ReactionRequestDto request = ReactionRequestDto.builder()
                .type(ReactionType.LIKE)
                .build();

        ReactionResponseDto response = ReactionResponseDto.builder()
                .type(ReactionType.LIKE)
                .user(UserDetailsResponseDto.builder().email("user1@x.com").build())
                .build();

        when(reactionService.reactToPost(eq("post-slug-1"), any(ReactionRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/reactions/post/post-slug-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldReactToComment()  throws Exception {
        ReactionRequestDto request = ReactionRequestDto.builder()
                .type(ReactionType.LIKE)
                .build();

        ReactionResponseDto response = ReactionResponseDto.builder()
                .type(ReactionType.LIKE)
                .user(UserDetailsResponseDto.builder().email("user1@x.com").build())
                .build();

        when(reactionService.reactToComment(eq("comment-slug-1"), any(ReactionRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/reactions/post/comment-slug-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldUpdateReaction() throws Exception {
        ReactionType reactionType= ReactionType.LOVE;

        ReactionResponseDto response = ReactionResponseDto.builder()
                .type(reactionType)
                .build();

        when(reactionService.updateReaction(eq("slug-1"), any(ReactionType.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/reactions/slug-1")
                        .param("type", reactionType.toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(reactionType.toString()));
    }

    @Test
    void shouldDeleteReaction() throws Exception {
        doNothing().when(reactionService).deleteReaction(anyString());

        mockMvc.perform(delete("/reactions/slug-1"))
                .andExpect(status().isNoContent());
    }



}
