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
 * A filter that extends the {@link UsernamePasswordAuthenticationFilter} to handle authentication through {@link JWT} tokens. <br/>
 * This filter must execute before {@link JwtAuthorizationFilter} to completely authenticate a user. It follows
 * the process below:
 *
 * <ol>
 *   <li>The method {@link #attemptAuthentication} will be the first to be called. This method will look for a valid {@link User} object
 *   in the request received. If the user cannot be built an exception will be thrown.</li>
 *   <li>The user will be converted to an {@link UsernamePasswordAuthenticationToken} and authenticated by the
 *   {@link AuthenticationManager} provided by Spring. </li>
 *   <li>The manager will send an {@link Authentication} object in case of success or throw an exception in case of a fail.</li>
 *   <li>If an exception is detected, then the framework automatically will call {@link #unsuccessfulAuthentication}.
 *   This method will handle the exception and send an {@link ErrorResponse} stopping the process.</li>
 *   <li>If the authentication succeeds then the method {@link #successfulAuthentication} will create a {@link JWT}
 *   token with the information of the user and append to the header of the response.</li>
 * </ol>
 *
 * <em>Note</em>: This filter will replace the default implementation, if added to the chain.
 *
 * @see UsernamePasswordAuthenticationFilter
 * @see UsernamePasswordAuthenticationToken
 * @see JwtAuthorizationFilter
 * @see AuthenticationManager
 * @see Authentication
 * @see ErrorResponse
 * @see JWT
 */
@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  /**
   * Initiates the process for an authentication. In the request should be at least an email and password field to
   * create a valid {@link User} object, then from the object an {@link UsernamePasswordAuthenticationToken} is created
   * and sent to the {@link AuthenticationManager} for further validation.
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      // An object mapper is used to recover a User object from the JSON object sent in the request
      User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

      Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

      // The manager will call an AuthenticationProvider that supports the type of the Authentication
      return authenticationManager.authenticate(authentication);
    } catch (IOException e) {
      // Thrown if the JSON object in the request cannot be mapped to a User
      throw new InvalidAuthenticationFieldsException();
    }
  }

  /**
   * Creates a {@link JWT} token if the authentication attempt was successful and adds it to the header "Authorization"
   * in the response.
   * The configurations for the token can be set in {@link SecurityConstants} or brand new could be added.
   *
   * @param request  the request received from past filters.
   * @param response the response to be sent back once all the filters were executed.
   * @param chain    a chain constituted from the next filters to be called.
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    // Creates a new JWT token for the client with a duration of 2 hours and the role specified on the validated Authentication
    String jwtToken = JWT.create()
        .withSubject(authResult.getName())
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION))
        .withClaim("role", authResult.getAuthorities().toString())
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET_KEY));

    response.addHeader("Authorization", SecurityConstants.BEARER + jwtToken);
  }

  /**
   * Sends an {@link ErrorResponse} in case of an unsuccessful authentication. This response is sent here because
   * the exact exception could be converted to other type of exception by next filters in the chain.<br/>
   * **WARNING** Security errors should not have personalized messages in order to not help attackers.
   * Remove or comment out this method to hide the personalized messages.
   *
   * @param request  the request received from past filters.
   * @param response the response to be sent back once all the filters were executed.
   * @param failed   the exception caught in the {@link #attemptAuthentication} method.
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    ErrorResponse errorResponse = new ErrorResponse(401, failed.getMessage());
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    // Gets the stream from the response
    OutputStream responseStream = response.getOutputStream();
    // Write with an object mapper the ErrorResponse object on the stream
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    mapper.writeValue(responseStream, errorResponse);
    responseStream.flush();
  }
}
