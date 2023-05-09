package com.raisedeel.foodappmanager.security;

import com.raisedeel.foodappmanager.security.filters.AuthenticationFilter;
import com.raisedeel.foodappmanager.security.filters.JwtAuthorizationFilter;
import com.raisedeel.foodappmanager.security.providers.CustomAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static com.raisedeel.foodappmanager.security.filters.AuthFilterConfigurer.authFilterConfigurer;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private CustomAuthenticationProvider customAuthenticationProvider;
  private JwtAuthorizationFilter authorizationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/user/register").permitAll()
        .requestMatchers("/user/upgrade/*").hasRole("ADMIN")
        .anyRequest().authenticated()
        .and()
        .authenticationProvider(customAuthenticationProvider)
        .apply(authFilterConfigurer())
        .and()
        .addFilterAfter(authorizationFilter, AuthenticationFilter.class)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    return http.build();
  }
}