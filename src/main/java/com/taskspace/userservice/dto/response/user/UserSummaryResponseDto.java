package com.taskspace.userservice.dto.response.user;

import java.util.UUID;

public record UserSummaryResponseDto(
        UUID id,
        String firstName,
        String lastName

) {
}
