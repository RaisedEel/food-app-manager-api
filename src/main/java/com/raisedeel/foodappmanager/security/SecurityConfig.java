package com.raisedeel.foodappmanager.security;

import com.raisedeel.foodappmanager.security.filters.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
    authenticationFilter.setFilterProcessesUrl("/user/authenticate");

    http
        .csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/user/register").permitAll()
        .anyRequest().authenticated()
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilter(authenticationFilter);

    return http.build();
  }

}
