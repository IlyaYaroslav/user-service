package com.taskspace.userservice.dto.requst;

import java.util.UUID;

public record UserUploadProfilePictureRequestDto(
        UUID id,
        byte[] uploadUserPhoto
) {
}
