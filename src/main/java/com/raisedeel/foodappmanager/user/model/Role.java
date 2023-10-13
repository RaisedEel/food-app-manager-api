package com.raisedeel.foodappmanager.user.model;

/**
 * Defines the roles for the different types of users. Each user has assigned 1 role of this set.
 * <p/>The available roles are:
 * <ul>
 *   <li><b>ROLE CLIENT:</b> The default role for all users. These users can create basic profiles and subscribe
 *   to the different restaurants.</li>
 *   <li><b>ROLE OWNER:</b> Owners are created by the administrator and have complete control over a restaurant,
 *   including adding multiple dishes to it.</li>
 *   <li><b>ROLE ADMIN:</b> Administrators have total control over the entire application, including the ability create
 *   and remove owners and delete restaurants.</li>
 * </ul>
 *
 * @see User
 * @see UserOwner
 */
public enum Role {
  /**
   * Indicates that the user is an administrator.
   */
  ROLE_ADMIN,
  /**
   * The default role for all users.
   */
  ROLE_CLIENT,
  /**
   * Indicates the user is the owner of a restaurant.
   */
  ROLE_OWNER
}
