package com.raisedeel.foodappmanager.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.raisedeel.foodappmanager.security.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith(SecurityConstants.BEARER)) {
      filterChain.doFilter(request, response);
      return;
    }

    String jwtToken = header.replace(SecurityConstants.BEARER, "");

    DecodedJWT validJwt = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET_KEY))
        .build()
        .verify(jwtToken);
    String username = validJwt.getSubject();
    String userRole = validJwt.getClaim("role")
        .asString()
        .replace("[", "")
        .replace("]", "");

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        username,
        null,
        List.of(new SimpleGrantedAuthority(userRole)));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

}
