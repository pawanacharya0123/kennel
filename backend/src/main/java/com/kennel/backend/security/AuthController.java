package com.kennel.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kennel.backend.entity.Role;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.RoleName;
import com.kennel.backend.security.dtos.LoginRequest;
import com.kennel.backend.security.dtos.LoginResponse;
import com.kennel.backend.security.dtos.SignupRequest;
import com.kennel.backend.service.RoleService;
import com.kennel.backend.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserEntityService userEntityService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;
    private final JwtIssuer jwtIssuer;
    private final JwtDecoder jwtDecoder;
    private final RoleService roleService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request){
        return authService.attemptLogin(request.getEmail(), request.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Validated SignupRequest request){
        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        RoleName roleName= RoleName.ROLE_ADMIN;

        Role role= roleService
                .findRoleByName(roleName)
                .orElseGet(()-> roleService.saveRole(
                        Role.builder().roleName(roleName).build())
                );
        userEntity.setRoles(Set.of(role));

        userEntityService.registerUser(userEntity);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            String newAccessToken = authService.tokenFromRefreshToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
