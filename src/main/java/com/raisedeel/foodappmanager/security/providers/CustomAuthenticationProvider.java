package com.raisedeel.foodappmanager.security.providers;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Custom AuthenticationProvider. Uses the UserDetailsService bean and is expected to work with a custom authentication filter.
 * The authentication manager will select this provider (if it's added on the SecurityFilterChain),
 * to deal with UsernamePasswordAuthenticationFilters.
 *
 * @see com.raisedeel.foodappmanager.security.SecurityConfig
 */
@AllArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private PasswordEncoder passwordEncoder;
  private UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());

    if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
      throw new BadCredentialsException("Incorrect password for the provided user");
    }

    return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
