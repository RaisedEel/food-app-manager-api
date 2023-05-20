package com.raisedeel.foodappmanager.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationFieldsException extends AuthenticationException {
  public InvalidAuthenticationFieldsException() {
    super("Authentication request rejected. Found invalid fields in authentication request");
  }
}
