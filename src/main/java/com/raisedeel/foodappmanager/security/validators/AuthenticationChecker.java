package com.raisedeel.foodappmanager.security.validators;

import com.raisedeel.foodappmanager.user.model.Role;
import com.raisedeel.foodappmanager.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;

/**
 * Allow the creation of a checker for any authentication object. The checker uses the template
 * method pattern to mark the steps of any check. The checker allow any object to be compared
 * against the authentication object if and only if an implementation for the object to be converted
 * to {@link User} is provided during the instantiation of the class, other methods can be overridden
 * as needed too.
 * Can be used to restrict users from accessing or manipulating other users resources.
 *
 * @param <T> the type of the Object to be checked against the authentication
 */
public abstract class AuthenticationChecker<T> {

  private final CrudRepository<T, Long> repository;

  /**
   * A repository must be provided. The repository will be used to find the object to be compared.
   *
   * @param repository contains the object that will be compared against the authentication object
   */
  public AuthenticationChecker(CrudRepository<T, Long> repository) {
    this.repository = repository;
  }

  /**
   * Checks if the id on the request belongs to an object related to the current authenticated user.
   * The steps are provided next:
   * <ol>
   *   <li>Looks for the admin role, if found returns true.</li>
   *   <li>Checks if it should look for an owner role, returns false if the role is not found. (Optional)</li>
   *   <li>Gets the entity with the corresponding id parameter and stores it in an optional.</li>
   *   <li>If the entity is null returns false.</li>
   *   <li>If the entity is not an user already it uses the convertEntityToUser method and goes to the next step.</li>
   *   <li>Finally checks if the user and authentication usernames are equal and returns the result.</li>
   * </ol>
   *
   * @param authentication the authentication object containing the data of the user.
   *                       Should be given an object provided by the framework.
   * @param id             the id of the object to be checked.
   * @param checkForOwner  a {@code boolean} that indicates if the method should look for an owner role
   * @return returns {@code true} if the user should access the resource.
   */
  public boolean check(Authentication authentication, Long id, boolean checkForOwner) {
    if (checkIfAdmin(authentication)) return true;
    if (checkForOwner && !checkIfOwner(authentication)) return false;

    Optional<T> entity = repository.findById(id);

    User user;
    if (entity.isEmpty()) {
      return false;
    } else if (entity.get() instanceof User) {
      user = (User) entity.get();
    } else {
      user = convertEntityToUser(entity.get());
    }

    return authentication.getName().equals(user.getUsername());
  }

  /**
   * Checks if the authentication has the provided role.
   *
   * @param authentication the authentication object containing the data of the user.
   * @param role           a role that will be converted to an authority.
   * @return {@code true} if the authentication has an authority with the provided role.
   */
  private boolean checkRole(Authentication authentication, Role role) {
    return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role.toString()));
  }

  /**
   * Helper method that checks for the admin role.
   *
   * @param authentication the authentication object containing the data of the user.
   * @return {@code true} if the authentication has an authority with the provided role.
   */
  private boolean checkIfAdmin(Authentication authentication) {
    return checkRole(authentication, Role.ROLE_ADMIN);
  }

  /**
   * Helper method that checks for the owner role.
   *
   * @param authentication the authentication object containing the data of the user.
   * @return {@code true} if the authentication has an authority with the provided role.
   */
  private boolean checkIfOwner(Authentication authentication) {
    return checkRole(authentication, Role.ROLE_OWNER);
  }

  /**
   * Converts the object to be checked into a user object. This mapper is needed in order to check
   * if the authentication and data in the request are related.
   *
   * @param entity the type parameter in the checker.
   * @return a {@code User} that can be checked against the authentication object.
   */
  protected abstract User convertEntityToUser(T entity);

}
