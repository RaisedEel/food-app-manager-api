package com.raisedeel.foodappmanager.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Template class for a friendly JSON error response. It includes the error code, timestamp of when the
 * error occurred  and a detailed error message.
 */
@Data
public class ErrorResponse {
  private int errorCode;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;
  private String[] message;

  public ErrorResponse(int errorCode, String... message) {
    this.errorCode = errorCode;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

}
