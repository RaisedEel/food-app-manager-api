package com.raisedeel.foodappmanager.security.validators;

import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AuthenticationValidator {

  // Method for checking if the user id on the request belongs to the authenticated user
  boolean checkUserId(Authentication authentication, Map<String, String> attributes);

  // Method for checking if the user registered in a restaurant belongs to the authenticated user
  boolean checkRestaurantOwner(Authentication authentication, Map<String, String> attributes);

  // Method for checking if the dish belongs on a restaurant that the authenticated user owns
  boolean checkDishOwner(Authentication authentication, Map<String, String> attributes);

}
