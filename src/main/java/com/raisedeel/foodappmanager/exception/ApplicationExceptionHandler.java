package com.raisedeel.foodappmanager.exception;

import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.exception.exceptions.InvalidOperationException;
import com.raisedeel.foodappmanager.exception.model.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * An {@link ResponseEntityExceptionHandler} annotated with {@link ControllerAdvice}. Handles all the exceptions expected
 * to be thrown by the controllers. <br/>
 * Every handler can catch a specific type of exception and send back a custom
 * {@link ErrorResponse} with the error code and the reason for the error.
 *
 * @see ErrorResponse
 */
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles all {@link AuthenticationException}s resolved by the {@link org.springframework.security.web.AuthenticationEntryPoint}
   * and caught by the controller advice (401).
   *
   * @param ex the {@link AuthenticationException} caught.
   * @return a {@link ResponseEntity} with the status of unauthorized.
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
    return new ResponseEntity<>(new ErrorResponse(401, ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handles exceptions thrown when an entity was not found or by a delete operation.
   *
   * @param ex the {@link EntityNotFoundException} or {@link EmptyResultDataAccessException} caught.
   * @return a {@link ResponseEntity} with the status of not found (404).
   */
  @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(Exception ex) {
    return new ResponseEntity<>(new ErrorResponse(404, ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  /**
   * Handles exceptions thrown when a constraint in a SQL table is broken by an operation.
   *
   * @param ex the {@link DataIntegrityViolationException} caught.
   * @return a {@link ResponseEntity} with the status of a bad request (400).
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(Exception ex) {
    return new ResponseEntity<>(
        new ErrorResponse(400, "Error found while updating the database: " + ex.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles any {@link RuntimeException} that haven't been handled by a previous method and the custom
   * exception {@link InvalidOperationException} thrown when the requirements for an operation haven't been fulfilled.
   *
   * @param ex the {@link Exception} caught.
   * @return a {@link  ResponseEntity} with the status of a bad request (400).
   */
  @ExceptionHandler({InvalidOperationException.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse> handleRuntimeException(Exception ex) {
    return new ResponseEntity<>(new ErrorResponse(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles any other {@link Exception} that could be thrown by an unexpected error in the controller.
   * **WARNING** Exceptions could still not be caught by this handler.
   *
   * @param ex the {@link Exception} caught.
   * @return a {@link  ResponseEntity} with the status of a server error (500).
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
    return new ResponseEntity<>(
        new ErrorResponse(500, "An unexpected error occurred: " + ex.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles exceptions thrown when the JSON fields of the request have invalid data.
   *
   * @return a {@link ResponseEntity} with the status of bad request (400)
   * and the different messages errors of all the invalid fields.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    // Convert validation argument errors from the request into an array of their error messages
    String[] errorMessages = ex.getBindingResult().getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toArray(String[]::new);

    return new ResponseEntity<>(
        new ErrorResponse(400, errorMessages),
        HttpStatus.BAD_REQUEST);
  }
}
