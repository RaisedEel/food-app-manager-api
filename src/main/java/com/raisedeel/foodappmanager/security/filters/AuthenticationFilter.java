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

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

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

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    String jwtToken = JWT.create()
        .withSubject(authResult.getName())
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION))
        .withClaim("role", authResult.getAuthorities().toString())
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET_KEY));

    response.addHeader("Authorization", SecurityConstants.BEARER + jwtToken);
  }

  // Security errors should not have personalized messages in order to stop attackers. Remove this method to hide the personalized messages
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
