package com.raisedeel.foodappmanager.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisedeel.foodappmanager.exception.exceptions.InvalidAuthenticationFieldsException;
import com.raisedeel.foodappmanager.exception.model.ErrorResponse;
import com.raisedeel.foodappmanager.security.SecurityConstants;
import com.raisedeel.foodappmanager.user.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Extends the UsernamePasswordAuthenticationFilter. Will replace the original filter and
 * create a UsernamePasswordToken to be handled for the custom authentication provider.
 * In a successful authentication will return a JWT token otherwise an ErrorResponse will be sent back.
 *
 * @see ErrorResponse
 */
@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  /**
   * Initiates the process for an authentication. In the request should be an email and password at minimum
   * to create a valid {@code User} object, then from the object an {@code UsernamePasswordAuthenticationToken} is created
   * and sent to the authenticationManager for further validation.
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

      Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
      return authenticationManager.authenticate(authentication);
    } catch (IOException e) {
      throw new InvalidAuthenticationFieldsException();
    }
  }

  /**
   * Adds a header "Authorization" with a newly created JWT token if the authentication process was successful.
   * The configurations for the token can be set in SecurityConstants or brand new could be added.
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    // Creates a new JWT token for the client with a duration of 2 hours and the role specified on the request
    String jwtToken = JWT.create()
        .withSubject(authResult.getName())
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION))
        .withClaim("role", authResult.getAuthorities().toString())
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET_KEY));

    response.addHeader("Authorization", SecurityConstants.BEARER + jwtToken);
  }

  /**
   * Sends an ErrorResponse in case of an unsuccessful authentication. This ErrorResponse is sent here because
   * the exact error reason will be lost if it's expected to be resolved by the ExceptionHandlerEntry.
   * **WARNING** Security errors should not have personalized messages in order to not help attackers.
   * Remove this method to hide the personalized messages.
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    ErrorResponse errorResponse = new ErrorResponse(401, failed.getMessage());
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    OutputStream responseStream = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    mapper.writeValue(responseStream, errorResponse);
    responseStream.flush();
  }
}
