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
 * Filter that extends the {@link OncePerRequestFilter}, so only runs once per request. Will authorize {@link JWT} tokens created
 * previously by the {@link JwtAuthenticationFilter} and found in the header of an incoming request.<br/>
 * <p>
 * This filter must execute after {@link JwtAuthenticationFilter} to completely authenticate a user. It follows
 * the process below:
 *
 * <ol>
 *   <li>The method {@link #doFilter} will be called automatically by the framework for each request.</li>
 *   <li>Will check if the request contains a header "Authorization" and that it contains a bearer token. If the check
 *   fails it skips the filter execution.</li>
 *   <li>Next, the token will be extracted from the bearer and will be decoded to extract the username and role.</li>
 *   <li>If the decoding succeeds the extracted data will be converted into an {@link Authentication} object, otherwise
 *   an exception will be thrown.</li>
 *   <li>Finally the authentication will be set in the {@link SecurityContextHolder} context. This will complete the
 *   authentication of the user for the current request.</li>
 * </ol>
 *
 * <em>Note</em>: This filter will catch any exception in this and any future filters in the chain.
 *
 * @see OncePerRequestFilter
 * @see JwtAuthenticationFilter
 * @see SecurityContextHolder
 * @see Authentication
 * @see JWT
 */
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  /**
   * Validates incoming requests with an {@link JWT} token appended. If the token is valid, extract the user data to
   * create a new {@link Authentication} object and set it into the security context.
   * If an exception is caught, an {@link ErrorResponse} object is sent back as JSON data with the error encountered.
   *
   * @param request     the request received from past filters.
   * @param response    the response to be sent back once all the filters were executed.
   * @param filterChain a chain constituted from the next filters to be called.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");

    // If the request do not have an Authorization header or a bearer token then we skip the
    // execution of the filter
    if (header == null || !header.startsWith(SecurityConstants.BEARER)) {
      filterChain.doFilter(request, response);
      return;
    }

    // Replaces the bearer string that appears at the start of any jwt token
    String jwtToken = header.replace(SecurityConstants.BEARER, "");

    try {
      // Decodes the jwt token using the secret key. Also verifies that the token corresponds to the key,
      // making it a valid token, otherwise throws an JWTVerificationException
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

      // The new UsernamePasswordAuthenticationToken that will hold the username and the role for the context
      Authentication authentication = new UsernamePasswordAuthenticationToken(
          username,
          null,
          List.of(new SimpleGrantedAuthority(userRole)));

      // Assigning the newly created Authentication to the context. This will authorize ONLY the current request
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);

    } catch (Exception ex) {
      // Handling of JWT token exclusive errors, stops these errors from appearing in console and instead
      // sends back an ErrorResponse
      ErrorResponse errorResponse = new ErrorResponse(403, "User could not be authorized");
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);

      OutputStream responseStream = response.getOutputStream();
      // Write with an object mapper the ErrorResponse object on the stream
      ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
      mapper.writeValue(responseStream, errorResponse);
      responseStream.flush();
    }

  }

}
