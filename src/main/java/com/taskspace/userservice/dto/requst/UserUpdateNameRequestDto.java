package com.taskspace.userservice.dto.requst;

public record UserUpdateNameRequestDto(
        String newFirstName,
        String newLastName
) {
}
