package com.taskspace.userservice.mapper;

import com.taskspace.userservice.dto.requst.UserRegisterRequestDto;
import com.taskspace.userservice.dto.response.UserRegistereResponseDto;
import com.taskspace.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(UserRegisterRequestDto userCreateRequestDto);

    UserRegistereResponseDto toDto(User user);
}
