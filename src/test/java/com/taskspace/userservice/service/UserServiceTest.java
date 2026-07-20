package com.taskspace.userservice.service;

import com.taskspace.userservice.dto.requst.UserUpdateNameRequestDto;
import com.taskspace.userservice.dto.requst.UserUpdatePasswordRequestDto;
import com.taskspace.userservice.dto.response.user.UserResponseUpdateCredentialsResponseDto;
import com.taskspace.userservice.dto.response.user.UserUpdatePasswordResponseDto;
import com.taskspace.userservice.dto.response.user.UserUploadProfilePictureResponseDto;
import com.taskspace.userservice.entity.User;
import com.taskspace.userservice.exception.PasswordIncorrectException;
import com.taskspace.userservice.mapper.UserMapper;
import com.taskspace.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private MultipartFile file;

    @InjectMocks
    private UserService userService;

    @Test
    void updateNamesUpdatesFirstAndOptionalLastName() {
        User user = user();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponseUpdateCredentialsResponseDto response = userService.updateNames(
                user.getId(), new UserUpdateNameRequestDto("New name", null)
        );

        assertEquals(user.getId(), response.id());
        assertEquals("New name", response.firstName());
        assertNull(response.lastName());
        assertEquals("New name", user.getFirstName());
        assertNull(user.getLastName());
    }

    @Test
    void updatePasswordEncodesNewPasswordAndReturnsOnlyId() {
        User user = user();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old-password", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("new-password")).thenReturn("new-hash");

        UserUpdatePasswordResponseDto response = userService.updatePassword(
                user.getId(), new UserUpdatePasswordRequestDto("old-password", "new-password")
        );

        assertEquals(user.getId(), response.id());
        assertEquals("new-hash", user.getPassword());
    }

    @Test
    void updatePasswordRejectsIncorrectOldPassword() {
        User user = user();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", user.getPassword())).thenReturn(false);

        assertThrows(PasswordIncorrectException.class, () -> userService.updatePassword(
                user.getId(), new UserUpdatePasswordRequestDto("wrong-password", "new-password")
        ));

        verify(passwordEncoder, never()).encode("new-password");
    }

    @Test
    void uploadPhotoReplacesOldObjectAndReturnsPresignedUrl() {
        User user = user();
        user.setProfilePictureObjectName("old-object");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fileStorageService.upload(file)).thenReturn("new-object");
        when(fileStorageService.getPresignedUrl("new-object")).thenReturn("https://minio/avatar");

        UserUploadProfilePictureResponseDto response = userService.uploadPhoto(user.getId(), file);

        assertEquals(user.getId(), response.id());
        assertEquals("https://minio/avatar", response.profilePicturePresignedUrl());
        assertEquals("new-object", user.getProfilePictureObjectName());
        verify(fileStorageService).delete("old-object");
    }

    @Test
    void deleteProfilePictureDeletesObjectAndClearsReference() {
        User user = user();
        user.setProfilePictureObjectName("avatar-object");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteProfilePicture(user.getId());

        verify(fileStorageService).delete("avatar-object");
        assertNull(user.getProfilePictureObjectName());
    }

    private User user() {
        return User.builder()
                .id(UUID.randomUUID())
                .email("user@example.com")
                .firstName("First")
                .lastName("Last")
                .password("old-hash")
                .role("USER")
                .build();
    }
}
