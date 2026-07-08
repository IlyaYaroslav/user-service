package com.taskspace.userservice.controller;

import com.taskspace.userservice.dto.requst.UserUpdateRequestDto;
import com.taskspace.userservice.dto.requst.UserUploadProfilePictureRequestDto;
import com.taskspace.userservice.dto.response.user.UserResponseSummaryDto;
import com.taskspace.userservice.dto.response.user.UserResponseUpdateCredentialsResponseDto;
import com.taskspace.userservice.dto.response.user.UserUploadProfilePictureResponseDto;
import com.taskspace.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public UserResponseSummaryDto getUser(@RequestParam UUID id) {
        return userService.getUserInfo(id);
    }

    @PatchMapping("")
    public UserResponseUpdateCredentialsResponseDto update(@RequestBody UserUpdateRequestDto userUpdateRequest) {
        return userService.userUpdate();
    }

    @PutMapping("")
    public UserUploadProfilePictureResponseDto uploadPhoto(@RequestBody UserUploadProfilePictureRequestDto userUploadProfilePictureRequest){
        return userService.uploadPhoto(userUploadProfilePictureRequest);
    }

    @DeleteMapping
    public void deleteProfilePicture(@RequestParam UUID id) {
        userService.deleteProfilePicture(id);
    }

}
