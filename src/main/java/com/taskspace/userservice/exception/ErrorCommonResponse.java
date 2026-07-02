package com.taskspace.userservice.exception;

import lombok.Builder;

@Builder
public record ErrorCommonResponse(
        String errorMessage
) {
}
