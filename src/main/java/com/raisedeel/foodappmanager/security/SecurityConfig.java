package com.raisedeel.foodappmanager.security;

import com.raisedeel.foodappmanager.dish.model.Dish;
import com.raisedeel.foodappmanager.dish.repository.DishRepository;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import com.raisedeel.foodappmanager.security.exception.AccessDeniedExceptionHandler;
import com.raisedeel.foodappmanager.security.exception.AuthenticationExceptionHandler;
import com.raisedeel.foodappmanager.security.filters.JwtFiltersConfigurer;
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

import static com.raisedeel.foodappmanager.security.filters.JwtFiltersConfigurer.filtersConfigurer;

/**
 * Contains the bean for a custom {@link SecurityFilterChain} for the app. This bean will handle all the http requests
 * by assigning what roles or specific users will have access to which endpoint. <br/>
 * Also adds custom validators logic using the {@link AuthenticationChecker} class to stop other users to access data which
 * doesn't belong to them.
 * <p>
 * This implementation in specific configures:
 * <ul>
 *   <li>Disables CSRF.</li>
 *   <li>Enable authentication for all endpoints except the register and the GET endpoints.</li>
 *   <li>Creates and assign {@link AuthenticationChecker}s in POST and PUT endpoints to restrict
 *   users from manipulating others users data.</li>
 *   <li>Adds a {@link CustomAuthenticationProvider} for the app.</li>
 *   <li>Adds a {@link AuthenticationExceptionHandler} for the app.</li>
 *   <li>Adds a {@link AccessDeniedExceptionHandler} for the app.</li>
 *   <li>Adds custom authentication and authorization filters for JWT tokens through the {@link JwtFiltersConfigurer}.</li>
 *   <li>Sets the session creation policy to stateless meaning that the user will have to authenticate
 *   again for each request.</li>
 * </ul>
 *
 * @see SecurityFilterChain
 * @see HttpSecurity
 * @see AuthenticationChecker
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private CustomAuthenticationProvider customAuthenticationProvider;
  private AuthenticationExceptionHandler authenticationExceptionHandler;
  private AccessDeniedExceptionHandler accessDeniedHandler;

  private UserRepository userRepository;
  private RestaurantRepository restaurantRepository;
  private DishRepository dishRepository;

  /**
   * Builds a custom {@link SecurityFilterChain} for the app using a {@link HttpSecurity} object provided by the framework. <br/>
   * Any request will have to pass through this chain which adds security checks to the different endpoints. <br/>
   * Other configurations can be set like management session options or disabling csrf.
   *
   * @param http Builder to create a custom {@link SecurityFilterChain} that secures endpoints.
   * @return A fully configured {@link SecurityFilterChain}.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // The next checkers where created to stop users from manipulating data from other users
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
        .authenticationProvider(customAuthenticationProvider) // This provider will handle JWT authentication
        .apply(filtersConfigurer()) //Adds the AuthenticationFilter and AuthorizationFilter to the chain
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationExceptionHandler) // Will handle primarily authentication exceptions thrown by any filter
        .accessDeniedHandler(accessDeniedHandler) // Will handle access denied exceptions thrown by authorization filters
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Any Restful api needs to be stateless

    return http.build();
  }
}