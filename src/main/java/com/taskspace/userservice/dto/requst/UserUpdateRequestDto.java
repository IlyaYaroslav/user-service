package com.taskspace.userservice.dto.requst;

public record UserUpdateRequestDto(
        String newFirstName,
        String newLastName
) {
}
