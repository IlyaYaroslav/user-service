package com.taskspace.userservice.dto.response.user;

import com.taskspace.userservice.dto.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseSummaryDto(
        UUID id,
        String email,
        UserRole role,
        String firstName,
        String lastName,
        String profilePicturePresignedUrl
) {
}
