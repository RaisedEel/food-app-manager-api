package com.raisedeel.foodappmanager.user.web;

import com.raisedeel.foodappmanager.user.dto.UserDto;
import com.raisedeel.foodappmanager.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Tag(name = "User Controller", description = "Handle registering, authentication and modification of users. Includes promotions and demotions of owners (Admin endpoints)")
@RestController
@RequestMapping("/user")
public class UserController {

  UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserDto> createUserHandler(@Valid @RequestBody UserDto userDto) {
    return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
  }

  // This handler method is never called is only here for documentation purposes
  @PostMapping("/authenticate")
  public ResponseEntity<HttpStatus> authenticateUserHandler(@RequestBody UserDto userDto) {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> retrieveUserHandler(@PathVariable Long id) {
    return new ResponseEntity<>(userService.retrieveUser(id), HttpStatus.OK);
  }

  @GetMapping("/owners")
  public ResponseEntity<List<UserDto>> retrieveOwnersHandler() {
    return new ResponseEntity<>(userService.retrieveOwners(), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUserHandler(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
    return new ResponseEntity<>(userService.updateUser(id, userDto), HttpStatus.OK);
  }

  @PutMapping("/promote/{userId}/restaurant/{restaurantId}")
  public ResponseEntity<HttpStatus> upgradeUserHandler(@PathVariable Long userId, @PathVariable Long restaurantId) {
    userService.upgradeUser(userId, restaurantId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/demote/{userId}")
  public ResponseEntity<HttpStatus> demoteUserHandler(@PathVariable Long userId) {
    userService.demoteUser(userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
