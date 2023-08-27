package com.raisedeel.foodappmanager.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisedeel.foodappmanager.exception.model.ErrorResponse;
import com.raisedeel.foodappmanager.security.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * This filter will authorize JWT tokens created previously by an authentication filter
 * and added as a header of an incoming request. The filter only runs once per request.
 *
 * @see AuthenticationFilter
 */
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  /**
   * Validates incoming requests with an JWT token appended. If the token is valid sets a new principal with the data
   * recovered from the token, otherwise an ErrorResponse object is sent back as JSON data with the error found.
   *
   * @param request     the request received from past filters
   * @param response    the response to be sent back once all the filters are executed
   * @param filterChain the filter chain to be followed
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith(SecurityConstants.BEARER)) {
      filterChain.doFilter(request, response);
      return;
    }

    // Replaces the bearer string that appears at the start of any jwt token
    String jwtToken = header.replace(SecurityConstants.BEARER, "");

    try {
      // Decodes the jwt token using the secret key. Also verifies that the token corresponds to the key,
      // making it a valid token, otherwise throws an error
      DecodedJWT validJwt = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET_KEY))
          .build()
          .verify(jwtToken);

      String username = validJwt.getSubject();
      // The claim role contains the role of the user, this will become its authority when creating
      // the user token
      String userRole = validJwt.getClaim("role")
          .asString()
          .replace("[", "")
          .replace("]", "");

      // The new UsernamePasswordAuthenticationToken for the context
      Authentication authentication = new UsernamePasswordAuthenticationToken(
          username,
          null,
          List.of(new SimpleGrantedAuthority(userRole)));

      // Assigning the newly created token to the context for performing the current request
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);

    } catch (Exception ex) {
      // Handling of JWT token exclusive errors, stops these errors from appearing in console and instead
      // sends back an ErrorResponse
      ErrorResponse errorResponse = new ErrorResponse(403, "User could not be authorized");
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);

      OutputStream responseStream = response.getOutputStream();
      ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
      mapper.writeValue(responseStream, errorResponse);
      responseStream.flush();
    }

  }

}
