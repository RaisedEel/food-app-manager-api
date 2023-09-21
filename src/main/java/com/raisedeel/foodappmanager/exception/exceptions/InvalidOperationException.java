package com.raisedeel.foodappmanager.exception.exceptions;

/**
 * An exception that is thrown when the user attempts operations not supported by the API.
 */
public class InvalidOperationException extends RuntimeException {
  public InvalidOperationException() {
    super("This user cannot execute this operation");
  }

  public InvalidOperationException(String message) {
    super("Error while executing the operation: " + message);
  }
}
