package com.raisedeel.foodappmanager.user.service;

import com.raisedeel.foodappmanager.user.dto.UserDto;

import java.util.List;

/**
 * A service interface for user management operations.<br/>
 * This interface provides methods for managing user data, including user creation, retrieval, update,
 * and promotion/demotion to restaurant ownership.
 */
public interface UserService {

  /**
   * Saves the user data recovered from the {@link UserDto} into the database.
   *
   * @param userDto the {@link UserDto} containing user data to be saved.
   * @return the saved {@link UserDto} with updated information.
   */
  UserDto createUser(UserDto userDto);

  /**
   * Retrieves all owners from the database.
   *
   * @return a list of {@link UserDto} representing all owners.
   */
  List<UserDto> retrieveOwners();

  /**
   * Retrieves the user with the given id.
   *
   * @param id the ID of the user to retrieve.
   * @return the {@link UserDto} representing the retrieved user.
   */
  UserDto retrieveUser(Long id);

  /**
   * Updates the user with the given id using the data in the {@link UserDto}.
   *
   * @param id      the ID of the user to update.
   * @param userDto the {@link UserDto} containing updated user data.
   * @return the updated {@link UserDto}.
   */
  UserDto updateUser(Long id, UserDto userDto);

  /**
   * Promotes a user to become an owner of a restaurant.
   *
   * @param userId       the ID of the user to be promoted.
   * @param restaurantId the ID of the restaurant where the user will be promoted to owner.
   */
  void promoteUser(Long userId, Long restaurantId);

  /**
   * Demotes an owner to a basic user.
   *
   * @param userId the ID of the owner to be demoted.
   */
  void demoteUser(Long userId);
}
