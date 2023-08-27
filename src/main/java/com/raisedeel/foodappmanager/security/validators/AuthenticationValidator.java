package com.raisedeel.foodappmanager.security.validators;

import org.springframework.security.core.Authentication;

/**
 * Functions for realizing security checks. These functions receive a Long to search for a resource to check against an Authentication object.
 * Use only for security checks on {@link com.raisedeel.foodappmanager.security.SecurityConfig}.
 */
public interface AuthenticationValidator {

  /**
   * Checks if the userId on the request belongs to the current authenticated user.
   *
   * @param authentication the authentication object containing the data of the user.
   *                       Should be given an object provided by the framework.
   * @param userId         the id of the user to be checked.
   * @return returns {@code true} if the user should access the resource.
   */
  boolean checkUserId(Authentication authentication, Long userId);


  /**
   * Checks if the authenticated user is the actual owner of the restaurant with the given id.
   * Only use this function if the owner should be the only user to access the resource.
   *
   * @param authentication the authentication object containing the data of the user.
   *                       Should be given an object provided by the framework.
   * @param restaurantId   the id of the restaurant to be checked.
   * @return returns {@code true} if the user should access the resource.
   */
  boolean checkRestaurantOwner(Authentication authentication, Long restaurantId);


  /**
   * Checks if the authenticated user is the actual owner of the restaurant where the dish of the id given belongs.
   * Only use this function if the owner should be the only user to access the resource.
   *
   * @param authentication the authentication object containing the data of the user.
   *                       Should be given an object provided by the framework.
   * @param dishId         the id of the dish to be checked.
   * @return returns {@code true} if the user should access the resource.
   */
  boolean checkDishOwner(Authentication authentication, Long dishId);

}
