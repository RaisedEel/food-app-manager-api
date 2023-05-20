package com.raisedeel.foodappmanager.exception.exceptions;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String entity) {
    super(entity + " could not be found");
  }

}
