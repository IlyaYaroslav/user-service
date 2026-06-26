package com.taskspace.userservice.dto.response;

import lombok.Builder;

@Builder
public record UserRegistereResponseDto(
        String name
) {
}
