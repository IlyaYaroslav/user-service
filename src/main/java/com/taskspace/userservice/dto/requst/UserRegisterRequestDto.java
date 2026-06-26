package com.taskspace.userservice.dto.requst;

import lombok.Builder;

@Builder
public record UserRegisterRequestDto(
        String name,
        String password,
        String email,
        String role
) {
}
