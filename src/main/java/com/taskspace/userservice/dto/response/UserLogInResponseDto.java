package com.taskspace.userservice.dto.response;

import lombok.Builder;

@Builder
public record UserLogInResponseDto(
        String accessToken
) {
}
