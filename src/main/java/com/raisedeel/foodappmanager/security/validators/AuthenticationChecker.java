package com.raisedeel.foodappmanager.security.validators;

import com.raisedeel.foodappmanager.user.model.Role;
import com.raisedeel.foodappmanager.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;

/**
 * Allows the creation of a checker of any authentication object. The checker uses the template
 * method pattern to mark the steps of any check. <br/>
 * The checker allow any object to be compared
 * against the authentication object if and only if an implementation for the object to be converted
 * to {@link User} is provided during the instantiation of the class, other methods can be overridden
 * as needed too.<br/>
 * Can be used to restrict users from accessing or manipulating other users resources.
 *
 * @param <T> The type of the Object to be checked against the authentication.
 */
public abstract class AuthenticationChecker<T> {

  private final CrudRepository<T, Long> repository;

  /**
   * Construct a AuthenticationChecker with the provided repository.
   * The repository will be used to find the object to be compared.
   *
   * @param repository The {@link CrudRepository} that will be used to fetch objects to
   *                   compare against the authentication object.
   */
  public AuthenticationChecker(CrudRepository<T, Long> repository) {
    this.repository = repository;
  }

  /**
   * Checks if the id on the request belongs to an object related to the current authenticated user.
   * The steps are provided below:
   * <ol>
   *   <li>Looks for the admin role, if found returns true.</li>
   *   <li>Checks if it should look for an owner role, returns false if the role is not found. (Optional)</li>
   *   <li>Gets the entity with the corresponding id parameter using the {@code fetchEntity} method and stores it in an optional.</li>
   *   <li>If the entity is null returns false.</li>
   *   <li>If the entity is not an {@link User} already it uses the {@code convertEntityToUser} method and goes to the next step.</li>
   *   <li>Finally checks if the {@link User} and authentication usernames are equal and returns the result.</li>
   * </ol>
   *
   * @param authentication The {@link Authentication} object containing the data of the user.
   *                       Should be given an object provided by the framework.
   * @param id             The id of the object to be checked.
   * @param checkForOwner  A {@code boolean} that indicates if the method should look for an owner role.
   * @return Returns {@code true} if the user should access the resource.
   */
  public boolean check(Authentication authentication, Long id, boolean checkForOwner) {
    if (checkIfAdmin(authentication)) return true;
    if (checkForOwner && !checkIfOwner(authentication)) return false;

    Optional<T> entity = fetchEntity(id);

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
   * @param authentication the {@link Authentication} object containing the data of the user.
   * @param role           a {@link Role} that will be converted to an authority.
   * @return {@code true} if the authentication has an authority with the provided role.
   */
  private boolean checkRole(Authentication authentication, Role role) {
    return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role.toString()));
  }

  /**
   * Helper method that checks for the admin role.
   *
   * @param authentication the {@link Authentication} object containing the data of the user.
   * @return {@code true} if the authentication has an authority with the provided role.
   */
  protected boolean checkIfAdmin(Authentication authentication) {
    return checkRole(authentication, Role.ROLE_ADMIN);
  }

  /**
   * Helper method that checks for the owner role.
   *
   * @param authentication the {@link Authentication} object containing the data of the user.
   * @return {@code true} if the authentication has an authority with the provided role.
   */
  protected boolean checkIfOwner(Authentication authentication) {
    return checkRole(authentication, Role.ROLE_OWNER);
  }

  /**
   * Fetch the object related with the authentication. The object is wrapped with an optional
   * in order to prevent the {@link NullPointerException}. <br/>
   * May be overridden if you do not want to use the default behavior that is fetching using
   * the repository provided in the constructor or if you have a custom repository implementation.
   *
   * @param id A {@code Long} that identifies the object.
   * @return An {@link Optional} containing the object of type {@code T} to be checked.
   */
  protected Optional<T> fetchEntity(Long id) {
    return repository.findById(id);
  }

  /**
   * Converts the object to be checked into a user object. This mapper is needed in order to check
   * if the authentication and data in the request are related.
   *
   * @param entity The object with type parameter {@code T}. This object will be provided automatically from the {@link CrudRepository}.
   * @return A {@link User} that can be checked against the {@link Authentication} object.
   */
  protected abstract User convertEntityToUser(T entity);

}
