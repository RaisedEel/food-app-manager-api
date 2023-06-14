package com.raisedeel.foodappmanager.security;

import com.raisedeel.foodappmanager.security.providers.CustomAuthenticationProvider;
import com.raisedeel.foodappmanager.security.validators.AuthenticationValidator;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
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
  private AuthenticationValidator authenticationValidator;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/user/register").permitAll()
        .requestMatchers(HttpMethod.GET, "/restaurant/**", "/dish/**", "/subscription/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/user/{id}").access(
            (authentication, context) ->
                new AuthorizationDecision(
                    authenticationValidator.checkUserId(authentication.get(), context.getVariables())
                )
        )
        .requestMatchers(HttpMethod.PUT, "/restaurant/{id}", "/dish/restaurant/{id}").access(
            (authentication, context) ->
                new AuthorizationDecision(
                    authenticationValidator.checkRestaurantOwner(authentication.get(), context.getVariables())
                )
        )
        .requestMatchers("dish/{id}").access(
            (authentication, context) ->
                new AuthorizationDecision(
                    authenticationValidator.checkDishOwner(authentication.get(), context.getVariables())
                )
        )
        .requestMatchers("/user/upgrade/**", "/user/demote/*", "/restaurant/remove/*").hasRole("ADMIN")
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