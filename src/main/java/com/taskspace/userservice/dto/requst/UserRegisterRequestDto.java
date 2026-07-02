package com.taskspace.userservice.dto.requst;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRegisterRequestDto(

        @NotBlank
        @NotEmpty
        String name,

        @NotBlank
        @NotEmpty
        @Size(min = 8, message = "Пароль не может быть меньше 8")
        String password,

        @Email
        @NotEmpty
        String email
) {
}
