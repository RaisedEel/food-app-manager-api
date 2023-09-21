package com.raisedeel.foodappmanager.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * An {@link AuthenticationException} that is thrown when a valid User could not be extracted from a request.
 */
public class InvalidAuthenticationFieldsException extends AuthenticationException {
  public InvalidAuthenticationFieldsException() {
    super("Authentication request rejected. Found invalid fields in authentication request");
  }
}
