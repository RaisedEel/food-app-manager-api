package com.raisedeel.foodappmanager.user.service;

import com.raisedeel.foodappmanager.user.dto.UserDto;
import com.raisedeel.foodappmanager.user.dto.UserMapper;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  UserRepository userRepository;
  UserMapper userMapper;

  @Override
  public UserDto createUser(UserDto userDto) {
    User user = userRepository.save(userMapper.dtoToUser(userDto));
    return userMapper.userToDto(user);
  }

  @Override
  public UserDto retrieveUser(Long id) {
    return userMapper.userToDto(getUserById(id));
  }

  @Override
  public UserDto updateUser(Long id, UserDto userDto) {
    User updatedUser = userMapper.updateFromDto(userDto, getUserById(id));
    return userMapper.userToDto(userRepository.save(updatedUser));
  }

  private User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No User Found"));
  }

}
