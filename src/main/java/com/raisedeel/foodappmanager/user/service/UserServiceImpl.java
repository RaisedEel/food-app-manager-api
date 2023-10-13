package com.raisedeel.foodappmanager.user.service;

import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.exception.exceptions.InvalidOperationException;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import com.raisedeel.foodappmanager.subscription.model.Subscription;
import com.raisedeel.foodappmanager.subscription.repository.SubscriptionRepository;
import com.raisedeel.foodappmanager.user.dto.UserDto;
import com.raisedeel.foodappmanager.user.dto.UserMapper;
import com.raisedeel.foodappmanager.user.model.Role;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.model.UserOwner;
import com.raisedeel.foodappmanager.user.repository.OwnerRepository;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link UserService} interface for user management operations.
 * This class provides concrete implementations of methods for managing user data, including user creation,
 * retrieval, update, and promotion/demotion to restaurant ownership. It collaborates with various repositories
 * and mappers to fulfill its functionality.
 *
 * @see UserService
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  UserRepository userRepository;
  OwnerRepository ownerRepository;
  RestaurantRepository restaurantRepository;
  SubscriptionRepository subscriptionRepository;

  PasswordEncoder passwordEncoder;
  UserMapper userMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  public UserDto createUser(UserDto userDto) {
    User user = userMapper.dtoToUser(userDto);
    //Encode the password
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.ROLE_CLIENT);
    return userMapper.userToDto(userRepository.save(user));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UserDto> retrieveOwners() {
    return ((List<UserOwner>) ownerRepository.findAll()).stream().map(userMapper::ownerToDto).toList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserDto retrieveUser(Long id) {
    return userMapper.userToDto(getUserById(id));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserDto updateUser(Long id, UserDto userDto) {
    User updatedUser = userMapper.updateFromDto(userDto, getUserById(id));

    if (userDto.getPassword() != null) {
      // Encode new password
      updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    }

    return userMapper.userToDto(userRepository.save(updatedUser));
  }

  /**
   * {@inheritDoc}
   * This method checks if the user's role is "Client" and if the restaurant doesn't already have an owner. <br/>
   * If these conditions are met, it transforms the user into a restaurant owner,
   * including the transfer of subscriptions.
   */
  @Override
  public void promoteUser(Long userId, Long restaurantId) {
    User user = getUserById(userId);
    Restaurant restaurant = getRestaurantById(restaurantId);

    if (!user.getRole().equals(Role.ROLE_CLIENT)) {
      throw new InvalidOperationException();
    }

    if (restaurant.getOwner() != null) {
      throw new InvalidOperationException("Restaurant already owned");
    }

    UserOwner userOwner = new UserOwner(
        null,
        user.getName(),
        user.getUsername(),
        user.getPassword(),
        user.getAddress(),
        restaurant
    );

    // Delete the basic user
    userRepository.delete(user);
    // Save the new owner user
    User savedOwner = userRepository.save(userOwner);

    // Transfer the subscriptions to the saved user
    for (Subscription subscription : user.getSubscriptions()) {
      subscription.setUser(savedOwner);
      subscriptionRepository.save(subscription);
    }
  }

  /**
   * {@inheritDoc}
   * This method transforms a restaurant owner into a basic user, including the transfer of subscriptions.
   */
  @Override
  public void demoteUser(Long userId) {
    UserOwner owner = ownerRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User"));

    User demotedUser = new User(
        null,
        owner.getName(),
        owner.getUsername(),
        owner.getPassword(),
        owner.getAddress(),
        Role.ROLE_CLIENT,
        null
    );

    // Delete the owner user
    ownerRepository.delete(owner);
    // Save the new basic user
    User savedUser = userRepository.save(demotedUser);

    // Transfer the subscriptions to the saved user
    for (Subscription subscription : owner.getSubscriptions()) {
      subscription.setUser(savedUser);
      subscriptionRepository.save(subscription);
    }
  }

  private User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User"));
  }

  private Restaurant getRestaurantById(Long id) {
    return restaurantRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Restaurant"));
  }

}
