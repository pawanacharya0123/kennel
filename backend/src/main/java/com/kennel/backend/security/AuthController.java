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
import com.kennel.backend.security.dtos.VerificationRequest;
import com.kennel.backend.service.RoleService;
import com.kennel.backend.service.UserEntityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest request){
        return ResponseEntity.ok(authService.attemptLogin(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Validated SignupRequest request){
        authService.register(request);

        return new ResponseEntity<>("User registered successfully and verification token sent to email!", HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            String newAccessToken = authService.tokenFromRefreshToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token!");
        }
    }

    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationRequest request) {
        authService.verifyEmail(request);
        return new ResponseEntity<>("User verified successfully!", HttpStatus.ACCEPTED);
    }

    @PatchMapping("/resend-verification-code")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try {
            authService.resendVerificationCode(email);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("User already verified!", HttpStatus.OK);
        }
        return new ResponseEntity<>("User verified code re-sent to email successfully!", HttpStatus.OK);
    }

    @PatchMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        authService.forgotPassword(email);
        return new ResponseEntity<>("User verified code re-sent to email successfully!", HttpStatus.OK);
    }

    @PutMapping("/verify-change-token")
    public ResponseEntity<?> verifyPasswordChangeToken(@RequestBody VerificationRequest request){
        authService.verifyPasswordChangeToken(request);
        return new ResponseEntity<>("User verified code verified successfully for password change!", HttpStatus.OK);
    }

    @PatchMapping("/set-password")
    public ResponseEntity<?> setNewPassword(@RequestBody @Validated LoginRequest request){
        authService.setNewPassword(request);
        return new ResponseEntity<>("User verified code re-sent to email successfully!", HttpStatus.OK);
    }
}
