package com.raisedeel.foodappmanager.user.dto;

import com.raisedeel.foodappmanager.restaurant.dto.RestaurantMapper;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.model.UserOwner;
import org.mapstruct.*;

/**
 * A Mapper interface annotated with {@link Mapper} that converts between {@link User} and {@link UserDto} objects using MapStruct.<br/>
 * This interface defines methods for mapping User objects to UserDto objects and vice versa.
 * It also provides a method for updating a User object from a UserDto while ignoring null values.
 * Additionally, this mapper utilizes {@link RestaurantMapper} for conversions involving Restaurant and RestaurantDto objects.
 *
 * @see Mapper
 * @see User
 * @see UserDto
 * @see RestaurantMapper
 */
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = RestaurantMapper.class
)
public interface UserMapper {

  /**
   * Maps a {@link User} object to a {@link UserDto} object.
   *
   * @param user the {@link User} object to be mapped to a {@link UserDto}.
   * @return the resulting {@link UserDto}.
   */
  UserDto userToDto(User user);

  /**
   * Maps a {@link UserOwner} object to a {@link UserDto} object.
   *
   * @param user the {@link UserOwner} object to be mapped to a {@link UserDto}.
   * @return the resulting {@link UserDto}.
   */
  UserDto ownerToDto(UserOwner user);

  /**
   * Maps a {@link UserDto} object to a {@link User} object.
   *
   * @param userDto the {@link UserDto} object to be mapped to a {@link User}.
   * @return the resulting {@link User} object.
   */
  User dtoToUser(UserDto userDto);

  /**
   * Updates a {@link User} object from a {@link UserDto} while ignoring null values.
   * This method is useful for selectively updating User attributes.
   *
   * @param userDto the {@link UserDto} containing updated information.
   * @param user    the {@link User} object to be updated.
   * @return the updated {@link User} object.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User updateFromDto(UserDto userDto, @MappingTarget User user);
}
