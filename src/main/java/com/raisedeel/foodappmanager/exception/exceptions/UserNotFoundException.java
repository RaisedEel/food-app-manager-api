package com.raisedeel.foodappmanager.exception.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("User could not be found");
  }

}
