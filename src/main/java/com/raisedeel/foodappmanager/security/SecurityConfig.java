package com.raisedeel.foodappmanager.security;

import com.raisedeel.foodappmanager.dish.model.Dish;
import com.raisedeel.foodappmanager.dish.repository.DishRepository;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import com.raisedeel.foodappmanager.security.providers.CustomAuthenticationProvider;
import com.raisedeel.foodappmanager.security.validators.AuthenticationChecker;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
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

  private UserRepository userRepository;
  private RestaurantRepository restaurantRepository;
  private DishRepository dishRepository;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    AuthenticationChecker<User> userChecker = new AuthenticationChecker<>(userRepository) {
      @Override
      protected User convertEntityToUser(User entity) {
        return entity;
      }
    };

    AuthenticationChecker<Restaurant> restaurantChecker =
        new AuthenticationChecker<>(restaurantRepository) {
          @Override
          protected User convertEntityToUser(Restaurant entity) {
            return entity.getOwner();
          }
        };

    AuthenticationChecker<Dish> dishChecker = new AuthenticationChecker<>(dishRepository) {
      @Override
      protected User convertEntityToUser(Dish entity) {
        return entity.getRestaurant() != null ? entity.getRestaurant().getOwner() : null;
      }
    };

    http
        .csrf().disable()// CSRF disabled
        .authorizeHttpRequests()
        .requestMatchers("/user/register").permitAll() // permitAll function will allow this endpoint to be accessed by non-authenticated users
        .requestMatchers(HttpMethod.GET, "/restaurant/**", "/dish/**", "/subscription/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/user/{id}").access(
            (authentication, context) ->
                new AuthorizationDecision(
                    userChecker.check(authentication.get(), Long.valueOf(context.getVariables().get("id")), false)
                )
        )
        .requestMatchers("/restaurant/{id}", "/dish/restaurant/{id}").access(
            (authentication, context) ->
                new AuthorizationDecision(
                    restaurantChecker.check(authentication.get(), Long.valueOf(context.getVariables().get("id")), true)
                )
        )
        .requestMatchers("/dish/{id}").access(
            (authentication, context) ->
                new AuthorizationDecision(
                    dishChecker.check(authentication.get(), Long.valueOf(context.getVariables().get("id")), true)
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