package com.raisedeel.foodappmanager.security;

/**
 * Simple JWT token settings class. Can change the secret key and expiration time for a valid token.
 */
public class SecurityConstants {

  // Default value for SECRET_KEY must be changed
  public static final String SECRET_KEY = "bQeThWmZq4t7w!z$C&F)J@NcRfUjXn2r5u8x/A?D*G-KaPdSgVkYp3s6v9y$B&E)";

  // 2 hours before expiration
  public static final int TOKEN_EXPIRATION = 7200000;
  public static final String BEARER = "Bearer ";
}
