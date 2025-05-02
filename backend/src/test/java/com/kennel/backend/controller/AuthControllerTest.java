package com.kennel.backend.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kennel.backend.entity.Role;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.RoleName;
import com.kennel.backend.security.*;
import com.kennel.backend.security.dtos.LoginRequest;
import com.kennel.backend.security.dtos.LoginResponse;
import com.kennel.backend.security.dtos.SignupRequest;
import com.kennel.backend.service.RoleService;
import com.kennel.backend.service.UserEntityService;
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
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserEntityService userEntityService;

    @TestConfiguration
    @EnableConfigurationProperties(JwtProperties.class)
    static class MockConfig {
        @Bean
        public AuthService authService() {
            return Mockito.mock(AuthService.class);
        }

        @Bean
        public RoleService roleService() {
            return Mockito.mock(RoleService.class);
        }

        @Bean
        public UserEntityService userEntityService() {
            return Mockito.mock(UserEntityService.class);
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
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest("user@example.com", "password123");

        LoginResponse response = new LoginResponse("access-token", "refresh-token");

        when(authService.attemptLogin(request.getEmail(), request.getPassword()))
                .thenReturn(response);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect( jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception{
        SignupRequest signupRequest= new SignupRequest("newuser@example.com", "securePassword");
        Role role = Role.builder().id(1L).roleName(RoleName.ROLE_ADMIN).build();

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(roleService.findRoleByName(any())).thenReturn(Optional.of(role));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully!"));

        verify(userEntityService).registerUser(any(UserEntity.class));
    }

    @Test
    void shouldRefreshAccessToken() throws Exception {
        String refreshToken = "validRefreshToken";
        String newAccessToken = "newAccessToken";

        when(authService.tokenFromRefreshToken(refreshToken)).thenReturn(newAccessToken);

        mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\": \"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken));
    }

    @Test
    void shouldFailRefreshWithInvalidToken() throws Exception {
        String refreshToken = "invalidToken";

        when(authService.tokenFromRefreshToken(refreshToken)).thenThrow(new JWTVerificationException("Invalid"));

        mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\": \"" + refreshToken + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid refresh token"));
    }
}
