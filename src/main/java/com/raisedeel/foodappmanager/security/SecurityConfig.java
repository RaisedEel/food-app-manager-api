package com.raisedeel.foodappmanager.security;

import com.raisedeel.foodappmanager.security.providers.CustomAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static com.raisedeel.foodappmanager.security.filters.FiltersConfigurer.filtersConfigurer;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private CustomAuthenticationProvider customAuthenticationProvider;
  private ExceptionHandlerEntry exceptionHandlerEntry;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/user/register").permitAll()
        .requestMatchers(HttpMethod.GET, "/restaurant/**", "dish/**").permitAll()
        .requestMatchers("/user/upgrade/**", "/user/demote/*", "/restaurant/remove/*").hasRole("ADMIN")
        .requestMatchers("/restaurant/*", "/dish/restaurant/*", "dish/*").hasAnyRole("ADMIN", "OWNER")
        .anyRequest().authenticated()
        .and()
        .authenticationProvider(customAuthenticationProvider)
        .apply(filtersConfigurer())
        .and()
        .exceptionHandling().authenticationEntryPoint(exceptionHandlerEntry)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    return http.build();
  }
}