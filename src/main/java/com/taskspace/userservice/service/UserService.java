package com.taskspace.userservice.service;

import com.taskspace.userservice.dto.requst.UserLoginRequestDto;
import com.taskspace.userservice.dto.requst.UserRegisterRequestDto;
import com.taskspace.userservice.dto.response.UserLogInResponseDto;
import com.taskspace.userservice.dto.response.UserRegistereResponseDto;
import com.taskspace.userservice.entity.User;
import com.taskspace.userservice.mapper.UserMapper;
import com.taskspace.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserRegistereResponseDto register(UserRegisterRequestDto userCreate) {

        User user = userRepository.save(userMapper.toEntity(userCreate));
        return userMapper.toDto(user);
    }

    public UserLogInResponseDto logIn(UserLoginRequestDto userCreateRequest) {
        return new UserLogInResponseDto("asd");

    }
}
