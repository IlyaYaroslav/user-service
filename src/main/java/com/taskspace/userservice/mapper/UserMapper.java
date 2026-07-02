package com.taskspace.userservice.mapper;

import com.taskspace.userservice.dto.requst.UserRegisterRequestDto;
import com.taskspace.userservice.dto.response.UserLogInResponseDto;
import com.taskspace.userservice.dto.response.UserRegisterResponseDto;
import com.taskspace.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRegisterRequestDto userCreateRequestDto);

    UserRegisterResponseDto toRegisterResponseDto(User user, String token);

    @Mapping(target = "accessToken", source = "token")
    UserLogInResponseDto toLoginResponseDto(User user, String token);
}
