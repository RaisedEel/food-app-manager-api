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

/**
 * Custom security configuration filter for the app. Will handle all the http requests by assigning what roles
 * will have access to which endpoint also adds custom validator logic to stop other users to access data which
 * doesn't belong to them.
 */
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
        .csrf().disable()// CSRF disabled
        .authorizeHttpRequests()
        .requestMatchers("/user/register").permitAll() // permitAll function will allow this endpoint to be accessed by non-authenticated users
        .requestMatchers(HttpMethod.GET, "/restaurant/**", "/dish/**", "/subscription/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/user/{id}").access(
            (authentication, context) ->
                new AuthorizationDecision(
                    authenticationValidator.checkUserId(authentication.get(), Long.valueOf(context.getVariables().get("id")))
                )
        )
        .requestMatchers(HttpMethod.PUT, "/restaurant/{id}", "/dish/restaurant/{id}").access(
            (authentication, context) ->
                new AuthorizationDecision(
                    authenticationValidator.checkRestaurantOwner(authentication.get(), Long.valueOf(context.getVariables().get("id")))
                )
        )
        .requestMatchers("dish/{id}").access(
            (authentication, context) ->
                new AuthorizationDecision(
                    authenticationValidator.checkDishOwner(authentication.get(), Long.valueOf(context.getVariables().get("id")))
                )
        )
        .requestMatchers("/user/upgrade/**", "/user/demote/*", "/restaurant/remove/*").hasRole("ADMIN")
        .anyRequest().authenticated() // Make all endpoint before this point ask for authentication
        .and()
        .authenticationProvider(customAuthenticationProvider) // **IMPORTANT** This provider will handle JWT authentication
        .apply(filtersConfigurer()) //Adds the AuthenticationFilter and AuthorizationFilter to the chain
        .and()
        .exceptionHandling().authenticationEntryPoint(exceptionHandlerEntry) // Will handle all exceptions thrown by the filter
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Any Restful api needs to be stateless

    return http.build();
  }
}