package com.raisedeel.foodappmanager.security.filters;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class FiltersConfigurer extends AbstractHttpConfigurer<FiltersConfigurer, HttpSecurity> {

  public static FiltersConfigurer filtersConfigurer() {
    return new FiltersConfigurer();
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(http.getSharedObject(AuthenticationManager.class));
    authenticationFilter.setFilterProcessesUrl("/user/authenticate");
    http.addFilter(authenticationFilter);
    http.addFilterAfter(new JwtAuthorizationFilter(), AuthenticationFilter.class);
  }
}
