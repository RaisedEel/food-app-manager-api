package com.raisedeel.foodappmanager.security;

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
 * An {@link AuthenticationEntryPoint} implementation that receives all the security exceptions thrown from the filter
 * chain, resolve it using an implementation of {@link HandlerExceptionResolver} and delegate it for handling to an exception handler like a ControllerAdvice. <br/>
 * The controller should have a handler for {@link AuthenticationException}s in order to send back a custom response
 * to the client.
 *
 * @see AuthenticationEntryPoint
 * @see HandlerExceptionResolver
 * @see AuthenticationException
 */
@Component
@AllArgsConstructor
public class ExceptionHandlerEntry implements AuthenticationEntryPoint {

  private HandlerExceptionResolver handlerExceptionResolver;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    handlerExceptionResolver.resolveException(request, response, null, authException);
  }

}
