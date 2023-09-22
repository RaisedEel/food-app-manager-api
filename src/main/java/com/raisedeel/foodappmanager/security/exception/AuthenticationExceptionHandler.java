package com.raisedeel.foodappmanager.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * An {@link AuthenticationEntryPoint} implementation that receives all the authentication exceptions thrown from the filter
 * chain. This implementation handles the exception using an implementation of {@link HandlerExceptionResolver} to delegate it to an
 * exception handler method in a ControllerAdvice. <br/>
 * The controller should have a handler for {@link AuthenticationException}s in order to send back a custom response
 * to the client.
 *
 * @see AuthenticationEntryPoint
 * @see HandlerExceptionResolver
 * @see AuthenticationException
 */
@Component
@AllArgsConstructor
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

  private HandlerExceptionResolver handlerExceptionResolver;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    handlerExceptionResolver.resolveException(request, response, null, authException);
  }

}
