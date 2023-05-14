package com.raisedeel.foodappmanager.user.service;

import com.raisedeel.foodappmanager.user.dto.UserDto;

import java.util.List;

public interface UserService {

  UserDto createUser(UserDto userDto);
  
  List<UserDto> retrieveOwners();

  UserDto retrieveUser(Long id);

  UserDto updateUser(Long id, UserDto userDto);

  void upgradeUser(Long id);
}
