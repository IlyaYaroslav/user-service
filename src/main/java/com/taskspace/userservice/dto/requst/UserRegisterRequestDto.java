package com.taskspace.userservice.dto.requst;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRegisterRequestDto(

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String password,

        @NotBlank
        @Email
        String email
) {
}
