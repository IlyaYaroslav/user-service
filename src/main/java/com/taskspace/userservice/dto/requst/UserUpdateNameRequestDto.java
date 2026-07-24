package com.taskspace.userservice.dto.requst;

import jakarta.validation.constraints.Size;

public record UserUpdateNameRequestDto(
        @Size(min = 1, max = 100)
        String newFirstName,

        @Size(min = 1, max = 100)
        String newLastName
) {
}
