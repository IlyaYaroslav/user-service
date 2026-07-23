package com.taskspace.userservice.controller;

import com.taskspace.userservice.dto.requst.UserUpdatePasswordRequestDto;
import com.taskspace.userservice.dto.requst.UserUpdateNameRequestDto;
import com.taskspace.userservice.dto.response.user.UserResponseSummaryDto;
import com.taskspace.userservice.dto.response.user.UserResponseUpdateCredentialsResponseDto;
import com.taskspace.userservice.dto.response.user.UserUpdatePasswordResponseDto;
import com.taskspace.userservice.dto.response.user.UserUploadProfilePictureResponseDto;
import com.taskspace.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserResponseSummaryDto getUser(@PathVariable UUID userId) {
        return userService.getUserInfo(userId);
    }

    @PatchMapping("/{userId}/name")
    public UserResponseUpdateCredentialsResponseDto updateNames(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateNameRequestDto userUpdateRequest
    ) {
        return userService.updateNames(userId, userUpdateRequest);
    }

    @PatchMapping("/{userId}/password")
    public UserUpdatePasswordResponseDto updatePassword(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdatePasswordRequestDto userUpdateRequest
    ) {
        return userService.updatePassword(userId, userUpdateRequest);
    }

    @PutMapping(value = "/{userId}/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserUploadProfilePictureResponseDto uploadPhoto(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file
    ) {
        return userService.uploadPhoto(userId, file);
    }

    @DeleteMapping("/{userId}/profile-picture")
    public ResponseEntity<Void> deleteProfilePicture(@PathVariable UUID userId) {
        userService.deleteProfilePicture(userId);
        return ResponseEntity.noContent().build();
    }
}
