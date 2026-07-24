package com.taskspace.userservice.service;

import com.taskspace.userservice.dto.requst.UserLoginRequestDto;
import com.taskspace.userservice.dto.requst.UserRegisterRequestDto;
import com.taskspace.userservice.dto.requst.UserUpdateNameRequestDto;
import com.taskspace.userservice.dto.requst.UserUpdatePasswordRequestDto;
import com.taskspace.userservice.dto.response.UserLogInResponseDto;
import com.taskspace.userservice.dto.response.UserRegisterResponseDto;
import com.taskspace.userservice.dto.response.user.UserResponseSummaryDto;
import com.taskspace.userservice.dto.response.user.UserResponseUpdateCredentialsResponseDto;
import com.taskspace.userservice.dto.response.user.UserUpdatePasswordResponseDto;
import com.taskspace.userservice.dto.response.user.UserUploadProfilePictureResponseDto;
import com.taskspace.userservice.entity.User;
import com.taskspace.userservice.exception.PasswordIncorrectException;
import com.taskspace.userservice.exception.user.EmailNotUniqueException;
import com.taskspace.userservice.exception.user.UserBadCredentialsException;
import com.taskspace.userservice.exception.user.UserNotFoundException;
import com.taskspace.userservice.mapper.UserMapper;
import com.taskspace.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passEncoder;
    private final JwtService jwtService;
    private final FileStorageService fileStorageService;

    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto userCreate) {
        if (userRepository.existsByEmail(userCreate.email())) {
            throw new EmailNotUniqueException(String.format("Email: %s is not unique", userCreate.email()));
        }

        User user = userMapper.toEntity(userCreate);
        user.setPassword(passEncoder.encode(userCreate.password()));
        userRepository.save(user);

        return userMapper.toRegisterResponseDto(user, jwtService.generateToken(user));
    }

    public UserLogInResponseDto logIn(UserLoginRequestDto userLoginRequest) {
        User user = userRepository.findByEmail(userLoginRequest.email())
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + userLoginRequest.email()));

        if (!passEncoder.matches(userLoginRequest.password(), user.getPassword())) {
            throw new UserBadCredentialsException("User`s credentials are incorrect");
        }

        return userMapper.toLoginResponseDto(user, jwtService.generateToken(user));

    }

    public UserResponseSummaryDto getUserInfo(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));


        String profilePicturePresignedUrl = fileStorageService.getPresignedUrl(user.getProfilePictureObjectName());
        return userMapper.toUserResponseSummaryDto(user, profilePicturePresignedUrl);
    }

    @Transactional
    public UserResponseUpdateCredentialsResponseDto updateNames(UUID userId, UserUpdateNameRequestDto userUpdateNameRequestDto) {
        User user = findUser(userId);
        user.setFirstName(userUpdateNameRequestDto.newFirstName());
        user.setLastName(userUpdateNameRequestDto.newLastName());

        return new UserResponseUpdateCredentialsResponseDto(user.getId(), user.getFirstName(), user.getLastName());
    }

    @Transactional
    public UserUploadProfilePictureResponseDto uploadPhoto(UUID userId, byte[] file, String contentType) {
        User user = findUser(userId);
        String oldObjectName = user.getProfilePictureObjectName();
        String newObjectName = fileStorageService.upload(file, contentType);
        user.setProfilePictureObjectName(newObjectName);

        if (oldObjectName != null) {
            fileStorageService.delete(oldObjectName);
        }

        return new UserUploadProfilePictureResponseDto(
                user.getId(),
                fileStorageService.getPresignedUrl(newObjectName)
        );
    }

    @Transactional
    public void deleteProfilePicture(UUID id) {
        User user = findUser(id);
        String objectName = user.getProfilePictureObjectName();
        fileStorageService.delete(objectName);
        user.setProfilePictureObjectName(null);
    }

    @Transactional
    public UserUpdatePasswordResponseDto updatePassword(UUID userId, UserUpdatePasswordRequestDto userUpdateRequest) {
        User user = findUser(userId);
        if (!passEncoder.matches(userUpdateRequest.oldPassword(), user.getPassword())) {
            throw new PasswordIncorrectException("");
        }

        user.setPassword(passEncoder.encode(userUpdateRequest.newPassword()));
        return new UserUpdatePasswordResponseDto(user.getId());
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + userId));
    }
}
