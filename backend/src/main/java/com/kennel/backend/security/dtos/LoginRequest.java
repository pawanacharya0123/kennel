package com.kennel.backend.security.dtos;

import com.kennel.backend.protection.customAnnotation.UniqueUserEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequest {
    @NotBlank(message = "Email can not be Blank.")
    @NotNull(message = "Email can not be null.")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "Password can not be Blank.")
    @NotNull(message = "Password can not be null.")
    @Size(min = 8, message = "Password must be over 8 characters long.")
    private String password;

}
