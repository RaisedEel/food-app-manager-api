package com.raisedeel.foodappmanager.user.service;

import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.user.dto.UserDto;
import com.raisedeel.foodappmanager.user.dto.UserMapper;
import com.raisedeel.foodappmanager.user.model.Role;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.model.UserOwner;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  UserRepository userRepository;
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
    return userRepository.findAllOwners().stream().map(userMapper::userToDto).toList();
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
  public void upgradeUser(Long id) {
    User user = getUserById(id);

    UserOwner userOwner = new UserOwner(
        null,
        user.getName(),
        user.getUsername(),
        user.getPassword(),
        user.getAddress(),
        Role.ROLE_OWNER,
        UUID.randomUUID().toString()
    );

    userRepository.delete(user);
    userRepository.save(userOwner);
  }

  private User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User"));
  }

}
