package com.kennel.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kennel.backend.entity.Role;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.RoleName;
import com.kennel.backend.security.dtos.*;
import com.kennel.backend.service.RoleService;
import com.kennel.backend.service.UserEntityService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok(authService.attemptLogin(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid SignupRequest request) throws BadRequestException {
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
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerificationRequest request) {
        authService.verifyEmail(request);
        return new ResponseEntity<>("User verified successfully!", HttpStatus.ACCEPTED);
    }

    @PostMapping("/resend-verification-code")
    public ResponseEntity<?> resendVerificationCode(@RequestBody Map<String, String> request) throws BadRequestException {
        String email = request.get("email");
        authService.resendVerificationCode(email);

        return new ResponseEntity<>("User verified code re-sent to email successfully!", HttpStatus.OK);
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) throws BadRequestException {
        String email = request.get("email");
        authService.forgotPassword(email);
        return new ResponseEntity<>("User verified code re-sent to email successfully!", HttpStatus.OK);
    }

    @PostMapping("/verify-change-token")
    public ResponseEntity<?> verifyPasswordChangeToken(@RequestBody @Valid VerificationRequest request) throws BadRequestException {
        String verifyPasswordChangeUUID = null;

        verifyPasswordChangeUUID = authService.verifyPasswordChangeToken(request);

        Map<String, String> response = new HashMap<>();
        response.put("token", verifyPasswordChangeUUID);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/set-new-password")
    public ResponseEntity<?> setNewPassword(@RequestBody @Valid NewPasswordRequest request) throws BadRequestException {
        authService.setNewPassword(request);
        return new ResponseEntity<>("User verified code re-sent to email successfully!", HttpStatus.OK);
    }
}
