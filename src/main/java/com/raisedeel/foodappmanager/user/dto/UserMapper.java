package com.raisedeel.foodappmanager.user.dto;

import com.raisedeel.foodappmanager.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring"
)
public interface UserMapper {
  UserDto userToDto(User user);

  User dtoToUser(UserDto userDto);
  
  User updateFromDto(UserDto userDto, @MappingTarget User user);
}
