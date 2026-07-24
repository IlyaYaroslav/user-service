package com.taskspace.userservice.controller;

import com.taskspace.userservice.dto.requst.UserRegisterRequestDto;
import com.taskspace.userservice.dto.requst.UserUpdateNameRequestDto;
import com.taskspace.userservice.dto.response.UserRegisterResponseDto;
import com.taskspace.userservice.dto.response.user.UserResponseUpdateCredentialsResponseDto;
import com.taskspace.userservice.dto.response.user.UserUploadProfilePictureResponseDto;
import com.taskspace.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerContractTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private AuthController authController;
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController, userController).build();
    }

    @Test
    void registerAcceptsFrontendNameField() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.register(org.mockito.ArgumentMatchers.any()))
                .thenReturn(UserRegisterResponseDto.builder()
                        .id(userId)
                        .token("token")
                        .build());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Alice",
                                  "email": "alice@example.com",
                                  "password": "Password1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));

        ArgumentCaptor<UserRegisterRequestDto> requestCaptor =
                ArgumentCaptor.forClass(UserRegisterRequestDto.class);
        verify(userService).register(requestCaptor.capture());
        assertEquals("Alice", requestCaptor.getValue().name());
    }

    @Test
    void uploadProfilePictureAcceptsRawFileBody() throws Exception {
        UUID userId = UUID.randomUUID();
        byte[] file = {1, 2, 3};
        when(userService.uploadPhoto(eq(userId), aryEq(file), eq(MediaType.IMAGE_PNG_VALUE)))
                .thenReturn(new UserUploadProfilePictureResponseDto(userId, "https://minio/avatar"));

        mockMvc.perform(put("/users/{userId}/profile-picture", userId)
                        .contentType(MediaType.IMAGE_PNG)
                        .content(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profilePicturePresignedUrl").value("https://minio/avatar"));

        verify(userService).uploadPhoto(eq(userId), aryEq(file), eq(MediaType.IMAGE_PNG_VALUE));
    }

    @Test
    void updateNamesAcceptsOnlyLastName() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.updateNames(eq(userId), any()))
                .thenReturn(new UserResponseUpdateCredentialsResponseDto(
                        userId,
                        "Alice",
                        "Smith"
                ));

        mockMvc.perform(patch("/users/{userId}/name", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "newLastName": "Smith"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Smith"));

        ArgumentCaptor<UserUpdateNameRequestDto> requestCaptor =
                ArgumentCaptor.forClass(UserUpdateNameRequestDto.class);
        verify(userService).updateNames(eq(userId), requestCaptor.capture());
        assertNull(requestCaptor.getValue().newFirstName());
        assertEquals("Smith", requestCaptor.getValue().newLastName());
    }

    @Test
    void updateNamesAcceptsEmptyLastNameForDeletion() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.updateNames(eq(userId), any()))
                .thenReturn(new UserResponseUpdateCredentialsResponseDto(
                        userId,
                        "Alice",
                        null
                ));

        mockMvc.perform(patch("/users/{userId}/name", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "newLastName": ""
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").doesNotExist());

        ArgumentCaptor<UserUpdateNameRequestDto> requestCaptor =
                ArgumentCaptor.forClass(UserUpdateNameRequestDto.class);
        verify(userService).updateNames(eq(userId), requestCaptor.capture());
        assertEquals("", requestCaptor.getValue().newLastName());
    }

    @Test
    void updateNamesRejectsEmptyFirstName() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(patch("/users/{userId}/name", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "newFirstName": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
