package com.taskspace.userservice.dto.response;

import lombok.Builder;

@Builder
public record UserRegisterResponseDto(
        String token
) {
}
