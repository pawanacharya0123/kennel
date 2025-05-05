package com.kennel.backend.security.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewPasswordRequest {
    @NotBlank(message = "Email can not be Blank.")
    @NotNull(message = "Email can not be null.")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "Password can not be Blank.")
    @NotNull(message = "Password can not be null.")
    @Size(min = 8, message = "Password must be over 8 characters long.")
    private String password;

    @NotBlank
    @NotNull
    private String passwordChangeToken;
}
