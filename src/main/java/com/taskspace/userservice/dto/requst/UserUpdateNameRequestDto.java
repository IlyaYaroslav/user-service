package com.taskspace.userservice.dto.requst;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateNameRequestDto(
        @NotBlank
        @Size(max = 100)
        String newFirstName,

        @Size(max = 100)
        String newLastName
) {
}
