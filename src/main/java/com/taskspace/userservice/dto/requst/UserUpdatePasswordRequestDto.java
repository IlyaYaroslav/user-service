package com.taskspace.userservice.dto.requst;

public record UserUpdatePasswordRequestDto (
        String oldPassword,
        String newPassword
) {
}
