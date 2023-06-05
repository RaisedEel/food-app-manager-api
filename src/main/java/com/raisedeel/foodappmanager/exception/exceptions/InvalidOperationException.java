package com.raisedeel.foodappmanager.exception.exceptions;

public class InvalidOperationException extends RuntimeException {
  public InvalidOperationException() {
    super("This user cannot execute this operation");
  }

  public InvalidOperationException(String message) {
    super("Error while executing the operation: " + message);
  }
}
