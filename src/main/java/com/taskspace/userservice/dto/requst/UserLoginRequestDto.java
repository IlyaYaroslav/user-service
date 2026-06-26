package com.taskspace.userservice.dto.requst;

public record UserLoginRequestDto(
        String name,
        String email,
        String password
) {
}
