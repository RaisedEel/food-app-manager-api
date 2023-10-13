package com.raisedeel.foodappmanager.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * An {@link AccessDeniedHandler} implementation that receives all the access denied exceptions thrown from the filter
 * chain. This implementation handles the exception using an implementation of {@link HandlerExceptionResolver} to delegate it to an
 * exception handler method in a ControllerAdvice. <br/>
 * The controller should have a handler defined for {@link AccessDeniedException}s in order to send back a custom response
 * to the client.
 *
 * @see AccessDeniedHandler
 * @see HandlerExceptionResolver
 * @see AccessDeniedException
 */
@Component
@AllArgsConstructor
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

  private HandlerExceptionResolver handlerExceptionResolver;

  /**
   * {@inheritDoc}
   *
   * @param request               that resulted in an <code>AccessDeniedException</code>
   * @param response              so that the user agent can be advised of the failure
   * @param accessDeniedException that caused the invocation
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
  }
}
