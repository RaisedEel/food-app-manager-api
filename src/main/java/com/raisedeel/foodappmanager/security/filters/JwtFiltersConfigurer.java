package com.raisedeel.foodappmanager.security.filters;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;

/**
 * An {@link AbstractHttpConfigurer} implementation that configures the {@link JwtAuthenticationFilter} and
 * {@link JwtAuthorizationFilter} and adds them to the security filter chain through the {@link HttpSecurity} object.<br/>
 * These filters are added to handle authentication and authorization of <strong>JWT tokens</strong>, a more secure
 * alternative to the default basic authentication of Spring Security.<br/><br/>
 * <p>
 * The process for the authentication is detailed below:
 * <ol>
 *   <li>The process start by registering a user in the route "{domain}/user/register".</li>
 *   <li>Once a user has been registered it can start the authentication by sending a request to
 *   "{domain}/user/authenticate". This will call the filter {@link JwtAuthenticationFilter}. </li>
 *   <li>The authentication filter will then check if the user exists in the database and create a JWT token using the
 *   information found if that is the case, otherwise an error response will send back.</li>
 *   <li>After the checks the filter will append the JWT token to the response using the Authorization header and sent it back to the user.</li>
 *   <li>An user with a JWT token can use it to access any of the resources of the API, this will call the filter
 *   {@link JwtAuthorizationFilter} to authorize it.</li>
 *   <li>The authorization filter will decode the token and check if the information recovered from the token belongs to
 *   the user of request.</li>
 *   <li>If the token is valid will authorize the request to access the resource, otherwise will send an error response back.</li>
 * </ol>
 *
 * @see AbstractHttpConfigurer
 * @see HttpSecurity
 * @see JwtAuthenticationFilter
 * @see JwtAuthorizationFilter
 */
public class JwtFiltersConfigurer extends AbstractHttpConfigurer<JwtFiltersConfigurer, HttpSecurity> {

  /**
   * Static factory method to create instances of {@link JwtFiltersConfigurer}.
   **/
  public static JwtFiltersConfigurer filtersConfigurer() {
    return new JwtFiltersConfigurer();
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // The authentication filter needs an Authentication Manager, in other to get it, it needs to be called here,
    // otherwise the manager might not be initialized
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(http.getSharedObject(AuthenticationManager.class));
    // Set the url that the filter will respond to
    jwtAuthenticationFilter.setFilterProcessesUrl("/user/authenticate");
    http.addFilter(jwtAuthenticationFilter);
    // The exception translator filter catch authentication exceptions thrown automatically
    http.addFilterAfter(new JwtAuthorizationFilter(), ExceptionTranslationFilter.class);
  }
}
