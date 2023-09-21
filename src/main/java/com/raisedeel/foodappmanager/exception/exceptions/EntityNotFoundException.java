package com.raisedeel.foodappmanager.exception.exceptions;

/**
 * Exception thrown when a repository could not find the requested resource.
 */
public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String entity) {
    super(entity + " could not be found");
  }

}
