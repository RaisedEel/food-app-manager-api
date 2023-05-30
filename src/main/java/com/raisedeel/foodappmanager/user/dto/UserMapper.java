package com.raisedeel.foodappmanager.user.dto;

import com.raisedeel.foodappmanager.restaurant.dto.RestaurantMapper;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionMapper;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.model.UserOwner;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {RestaurantMapper.class, SubscriptionMapper.class}
)
public interface UserMapper {
  UserDto userToDto(User user);

  UserDto ownerToDto(UserOwner user);

  User dtoToUser(UserDto userDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User updateFromDto(UserDto userDto, @MappingTarget User user);
}
