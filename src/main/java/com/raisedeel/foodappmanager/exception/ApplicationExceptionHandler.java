package com.raisedeel.foodappmanager.exception;

import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.exception.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> authenticationExceptionHandler(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorResponse(401, ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> entityNotFoundException(Exception ex) {
    return new ResponseEntity<>(new ErrorResponse(404, ex.getMessage()), HttpStatus.NOT_FOUND);
  }
}
