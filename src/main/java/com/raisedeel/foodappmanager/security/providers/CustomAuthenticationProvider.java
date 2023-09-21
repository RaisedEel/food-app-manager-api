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
 * Custom implementation of an {@link AuthenticationProvider}. Uses the {@link UserDetailsService} bean and is expected
 * to work with a custom authentication filter.<br/>
 * The default {@link org.springframework.security.authentication.AuthenticationManager}
 * will select this provider (if it's added on the SecurityFilterChain), to deal with {@link UsernamePasswordAuthenticationToken}s.
 *
 * @see com.raisedeel.foodappmanager.security.SecurityConfig
 */
@AllArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private PasswordEncoder passwordEncoder;
  private UserDetailsService userDetailsService;

  /**
   * When called will get an {@link UserDetails} from the database using the {@link UserDetailsService}. This user will
   * be recovered using the {@link Authentication} received from an authentication filter. <br/>
   * Then will check if both the passwords from the user and the authentication coincide and return
   * a {@link UsernamePasswordAuthenticationToken}, if the check passed.
   */
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
