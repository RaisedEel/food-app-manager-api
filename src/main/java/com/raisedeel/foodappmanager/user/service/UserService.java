package com.raisedeel.foodappmanager.user.service;

import com.raisedeel.foodappmanager.user.dto.UserDto;

public interface UserService {
  UserDto createUser(UserDto userDto);

  UserDto retrieveUser(Long id);

  UserDto updateUser(Long id, UserDto userDto);

  void upgradeUser(Long id);
}
