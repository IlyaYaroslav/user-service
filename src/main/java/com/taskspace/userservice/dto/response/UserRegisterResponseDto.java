package com.taskspace.userservice.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserRegisterResponseDto(
        UUID id,
        String token
) {
}
