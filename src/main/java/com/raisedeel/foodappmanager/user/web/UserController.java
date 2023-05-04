package com.raisedeel.foodappmanager.user.web;

import com.raisedeel.foodappmanager.user.dto.UserDto;
import com.raisedeel.foodappmanager.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

  UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserDto> createUserHandler(@RequestBody UserDto userDto) {
    return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> retrieveUserHandler(@PathVariable Long id) {
    return new ResponseEntity<>(userService.retrieveUser(id), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUserHandler(@PathVariable Long id, @RequestBody UserDto userDto) {
    return new ResponseEntity<>(userService.updateUser(id, userDto), HttpStatus.OK);
  }

}
