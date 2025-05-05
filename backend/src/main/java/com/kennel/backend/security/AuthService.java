package com.kennel.backend.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.kennel.backend.entity.ForgetPassword;
import com.kennel.backend.entity.OtpVerification;
import com.kennel.backend.entity.Role;
import com.kennel.backend.entity.UserEntity;
import com.kennel.backend.entity.enums.RoleName;
import com.kennel.backend.exception.InvalidOtpException;
import com.kennel.backend.repository.ForgetPasswordRepository;
import com.kennel.backend.repository.OtpVerificationRepository;
import com.kennel.backend.security.dtos.*;
import com.kennel.backend.service.RoleService;
import com.kennel.backend.service.UserEntityService;
import com.kennel.backend.utility.mail.MailgunEmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final JwtDecoder jwtDecoder;
    private final UserEntityService userEntityService;

    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final OtpVerificationRepository otpVerificationRepository;
    private final MailgunEmailService mailgunEmailService;

    private final ForgetPasswordRepository forgetPasswordRepository;

    public LoginResponse attemptLogin(String email, String password){
        var authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal= (UserPrincipal)authentication.getPrincipal();

        if (!principal.isVerified()) {
            throw new InvalidOtpException("Account is not verified. Please verify your email.");
        }

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var accessToken = jwtIssuer.issuer(principal.getUserId(), principal.getEmail(), roles);
        var refreshToken= jwtIssuer.issueRefreshToken(principal.getUserId(), principal.getEmail());
        return LoginResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public String tokenFromRefreshToken(String refreshToken){
        DecodedJWT decodedJWT = jwtDecoder.decode(refreshToken);

        String email = decodedJWT.getSubject();
        Long userId = decodedJWT.getClaim("userId").asLong();
        UserEntity userEntity = userEntityService
                .getUserEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<String> roles= userEntity.getRoles()
                .stream()
                .map(Role::getRoleName)
                .map(Enum::toString)
                .map(SimpleGrantedAuthority::new)
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();
        return jwtIssuer.issuer(userId, email, roles);
    }

    @Transactional
    public void register(SignupRequest request) throws BadRequestException {

        if (userEntityService.existsByEmail(request.getEmail())){
            throw new BadRequestException("UserName already exists.");
        }

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

        long otp = ThreadLocalRandom.current().nextLong(100000, 1000000);
        OtpVerification otpVerification = getOtpVerification(otp);

        otpVerification.setUser(userEntity);
        userEntity.setOtpVerification(otpVerification);

        mailgunEmailService.sendOtpEmail(userEntity.getEmail(), otp, "Registration Token");

        userEntityService.registerUser(userEntity);
    }

    @Transactional
    public void verifyEmail(VerificationRequest request) {
        UserEntity userEntity = userEntityService
                .getUserEntityByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getEmail()));

        Date now = new Date();
        Date createdAt = userEntity.getOtpVerification().getOtpCreatedAt();
        long diffInMillis = now.getTime() - createdAt.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);

        if (!userEntity.getOtpVerification().getOtp().equals(request.getOtp())
                || diffInMinutes > 10) {
            throw new InvalidOtpException("Invalid or expired OTP");
        }

        userEntity.setVerified(true);
        otpVerificationRepository.delete(userEntity.getOtpVerification());
        userEntity.setOtpVerification(null);
        userEntityService.saveUser(userEntity);
    }

    @Transactional
    public void resendVerificationCode(String email) throws BadRequestException {
        UserEntity userEntity = userEntityService
                .getUserEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if(userEntity.isVerified()) {
            throw new BadRequestException("User already Verified!");
        };

        long otp = ThreadLocalRandom.current().nextLong(100000, 1000000);
        OtpVerification otpVerification = getOtpVerification(otp);

        if(userEntity.getOtpVerification() !=null){
            otpVerificationRepository.delete(userEntity.getOtpVerification());
        }
        userEntity.setVerified(false);
        otpVerification.setUser(userEntity);
        userEntity.setOtpVerification(otpVerification);

        mailgunEmailService.sendOtpEmail(userEntity.getEmail(), otp, "Re-sent Registration Token");

        userEntityService.registerUser(userEntity);
    }

    public void forgotPassword(String email) throws BadRequestException {
        UserEntity userEntity = userEntityService
                .getUserEntityByEmail(email)
                //and isVerified=true
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (!userEntity.isVerified()){
            throw new BadRequestException("User is not verified through email. Try registering it again vai email verification!");
        }

        if(userEntity.getForgetPassword() != null){
            forgetPasswordRepository.delete(userEntity.getForgetPassword());
            userEntity.setForgetPassword(null);
        }

        long otp = ThreadLocalRandom.current().nextLong(100000, 1000000);
        ForgetPassword forgetPassword = ForgetPassword.builder()
                .otp(otp)
                .otpCreatedAt(new Date())
                .build();

        forgetPassword.setUser(userEntity);
        ForgetPassword savedForgetPassword = forgetPasswordRepository.save(forgetPassword);
        userEntity.setForgetPassword(savedForgetPassword);

        mailgunEmailService.sendOtpEmail(userEntity.getEmail(), otp, "Forgot Password Token");

        userEntityService.saveUser(userEntity);
    }

    public String verifyPasswordChangeToken(VerificationRequest request) throws BadRequestException {
        UserEntity userEntity = userEntityService
                .getUserEntityByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getEmail()));

        if (!userEntity.isVerified()){
            throw new BadRequestException("User is not verified through email. Try registering it again vai email verification!");
        }

        ForgetPassword forgetPassword = userEntity.getForgetPassword();

        Date now = new Date();
        Date createdAt = forgetPassword.getOtpCreatedAt();
        long diffInMillis = now.getTime() - createdAt.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);

        if (!forgetPassword.getOtp().equals(request.getOtp())
                || diffInMinutes > 10) {
            throw new InvalidOtpException("Invalid or expired OTP");
        }

        forgetPassword.setUUID(UUID.randomUUID().toString());
        forgetPassword.setUUIDCreatedAt(new Date());

        forgetPasswordRepository.save(forgetPassword);
        return forgetPassword.getUUID();
    }

    public void setNewPassword(NewPasswordRequest request) throws BadRequestException {
        UserEntity userEntity = userEntityService
                .getUserEntityByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getEmail()));

        ForgetPassword forgetPassword = userEntity.getForgetPassword();

        if(forgetPassword == null){
            throw new BadRequestException("No token assigned to this email. Re-apply for this token!");
        }

        Date now = new Date();
        Date createdAt = forgetPassword.getOtpCreatedAt();
        long diffInMillis = now.getTime() - createdAt.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);

        if(!forgetPassword.getUUID().equals(request.getPasswordChangeToken()) || diffInMinutes > 10){
            throw new InvalidOtpException("Invalid or expired OTP");
        }

        forgetPasswordRepository.delete(forgetPassword);
        userEntity.setForgetPassword(null);

        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setVerified(true);
        userEntityService.saveUser(userEntity);
    }

    private OtpVerification getOtpVerification(Long otp) {
        return OtpVerification.builder()
                .otpCreatedAt(new Date())
                .otp(otp)
                .build();
    }

}
