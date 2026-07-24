package com.taskspace.userservice.dto.requst;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateNameRequestDto(
        @Size(min = 1, max = 100)
        @Pattern(regexp = ".*\\S.*", message = "First name must not be blank")
        String newFirstName,

        @Size(max = 100)
        String newLastName
) {
}
