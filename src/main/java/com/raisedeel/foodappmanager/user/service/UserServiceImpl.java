package com.raisedeel.foodappmanager.user.service;

import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
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

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  UserRepository userRepository;
  OwnerRepository ownerRepository;
  RestaurantRepository restaurantRepository;
  PasswordEncoder passwordEncoder;
  UserMapper userMapper;

  @Override
  public UserDto createUser(UserDto userDto) {
    User user = userMapper.dtoToUser(userDto);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.ROLE_CLIENT);
    return userMapper.userToDto(userRepository.save(user));
  }

  @Override
  public List<UserDto> retrieveOwners() {
    return ((List<UserOwner>) ownerRepository.findAll()).stream().map(userMapper::ownerToDto).toList();
  }

  @Override
  public UserDto retrieveUser(Long id) {
    return userMapper.userToDto(getUserById(id));
  }

  @Override
  public UserDto updateUser(Long id, UserDto userDto) {
    User updatedUser = userMapper.updateFromDto(userDto, getUserById(id));

    if (userDto.getPassword() != null) {
      updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    }

    return userMapper.userToDto(userRepository.save(updatedUser));
  }

  @Override
  public void upgradeUser(Long userId, Long restaurantId) {
    User user = getUserById(userId);
    Restaurant restaurant = getRestaurantById(restaurantId);

    if (!user.getRole().equals(Role.ROLE_CLIENT)) {
      throw new RuntimeException("This operation cannot be executed");
    }

    if (restaurant.getOwner() != null) {
      throw new RuntimeException("Restaurant already owned");
    }

    UserOwner userOwner = new UserOwner(
        null,
        user.getName(),
        user.getUsername(),
        user.getPassword(),
        user.getAddress(),
        Role.ROLE_OWNER,
        restaurant
    );

    userRepository.delete(user);
    userRepository.save(userOwner);
  }

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
        Role.ROLE_CLIENT
    );

    ownerRepository.delete(owner);
    userRepository.save(demotedUser);
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
