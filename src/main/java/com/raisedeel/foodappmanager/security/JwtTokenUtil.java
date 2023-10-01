package com.raisedeel.foodappmanager.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

/**
 * A utility class for handling JSON Web Tokens (JWT).
 * <p>
 * This class provides static methods for creating and extracting useful information from JWT tokens.
 * Custom settings can be configured by modifying the following variables:
 *
 * <ul>
 *   <li>{@link #SECRET_KEY}: Defines the signature key for JWT tokens.</li>
 *   <li>{@link #TOKEN_EXPIRATION}: Defines the token's expiration time in milliseconds (default: 2 hours - 7,200,000 milliseconds).</li>
 *   <li>{@link #BEARER}: Defines the token type and marks its beginning (default: "Bearer").</li>
 * </ul>
 *
 * @see JWT
 */
public class JwtTokenUtil {

  // Default value for SECRET_KEY. MUST BE CHANGED FOR PRODUCTION
  public static final String SECRET_KEY = "bQeThWmZq4t7w!z$C&F)J@NcRfUjXn2r5u8x/A?D*G-KaPdSgVkYp3s6v9y$B&E)";

  // 2 hours before expiration
  public static final int TOKEN_EXPIRATION = 7_200_000;
  public static final String BEARER = "Bearer ";

  /**
   * Creates a {@link JWT} token using the username provided as the subject and
   * the role as a custom claim.
   *
   * @param username the subject for the token.
   * @param role     the role for the subject.
   * @return a string containing a brand-new JWT token.
   */
  public static String createToken(String username, String role) {
    return BEARER + JWT.create()
        .withSubject(username)
        .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
        .withClaim("role", role)
        .sign(Algorithm.HMAC512(SECRET_KEY));
  }

  /**
   * Recovers the subject/username from a {@link JWT} token. <br/>
   * Will check if the token is valid beforehand.
   *
   * @param jwtToken the {@link JWT} token string.
   * @return the string containing only the subject.
   * @throws JWTVerificationException when a token is invalid (e.g. if the signature or claims are wrong).
   */
  public static String getSubjectFromToken(String jwtToken) {
    String token = jwtToken.replace(BEARER, "");
    return JWT.require(Algorithm.HMAC512(SECRET_KEY))
        .build()
        .verify(token)
        .getSubject();
  }

  /**
   * Recovers the claim "role". The role represents the authority given to the owner of the token. <br/>
   * Will check if the token is valid beforehand.
   *
   * @param jwtToken the {@link JWT} token string.
   * @return the string containing only the role.
   * @throws JWTVerificationException when a token is invalid (e.g. if the signature or claims are wrong).
   */
  public static String getRoleFromToken(String jwtToken) {
    String token = jwtToken.replace(BEARER, "");
    return JWT.require(Algorithm.HMAC512(SECRET_KEY))
        .build()
        .verify(token)
        .getClaim("role")
        .asString();
  }
}
