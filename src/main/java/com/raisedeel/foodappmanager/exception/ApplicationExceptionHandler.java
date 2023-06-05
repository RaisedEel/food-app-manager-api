package com.raisedeel.foodappmanager.exception;

import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.exception.exceptions.InvalidOperationException;
import com.raisedeel.foodappmanager.exception.model.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
    return new ResponseEntity<>(new ErrorResponse(401, ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(Exception ex) {
    return new ResponseEntity<>(new ErrorResponse(404, ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(Exception ex) {
    return new ResponseEntity<>(
        new ErrorResponse(400, "Error found while updating the database: " + ex.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidOperationException.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse> handleRuntimeException(Exception ex) {
    return new ResponseEntity<>(new ErrorResponse(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
    return new ResponseEntity<>(
        new ErrorResponse(500, "An unexpected error occurred: " + ex.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
