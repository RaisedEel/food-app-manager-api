package com.raisedeel.foodappmanager.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
  private Long id;
  private String username;
  private String email;
  @JsonIgnore
  private String password;
  private String address;
}
