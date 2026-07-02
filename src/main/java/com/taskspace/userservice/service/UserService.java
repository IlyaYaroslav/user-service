package com.taskspace.userservice.service;

import com.taskspace.userservice.dto.requst.UserLoginRequestDto;
import com.taskspace.userservice.dto.requst.UserRegisterRequestDto;
import com.taskspace.userservice.dto.response.UserLogInResponseDto;
import com.taskspace.userservice.dto.response.UserRegisterResponseDto;
import com.taskspace.userservice.entity.User;
import com.taskspace.userservice.exception.user.EmailNotUniqueException;
import com.taskspace.userservice.exception.user.UserBadCredentialsException;
import com.taskspace.userservice.exception.user.UserNotFoundException;
import com.taskspace.userservice.mapper.UserMapper;
import com.taskspace.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passEncoder;
    private final JwtService jwtService;

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
}
