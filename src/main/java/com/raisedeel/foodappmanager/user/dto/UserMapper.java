package com.raisedeel.foodappmanager.user.dto;

import com.raisedeel.foodappmanager.user.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring"
)
public interface UserMapper {
  UserDto userToDto(User user);

  User dtoToUser(UserDto userDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User updateFromDto(UserDto userDto, @MappingTarget User user);
}
