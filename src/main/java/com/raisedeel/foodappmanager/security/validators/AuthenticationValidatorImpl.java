package com.raisedeel.foodappmanager.security.validators;

import com.raisedeel.foodappmanager.dish.model.Dish;
import com.raisedeel.foodappmanager.dish.repository.DishRepository;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import com.raisedeel.foodappmanager.user.model.Role;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Simple implementation for AuthenticationValidator. Simple checks to prevent users of accessing other users data
 *
 * @see AuthenticationValidator
 */
@AllArgsConstructor
@Service
public class AuthenticationValidatorImpl implements AuthenticationValidator {

  private UserRepository userRepository;
  private RestaurantRepository restaurantRepository;
  private DishRepository dishRepository;

  @Override
  public boolean checkUserId(Authentication authentication, Long userId) {
    // Allow access to the admin
    if (containsRole(authentication, Role.ROLE_ADMIN.toString()))
      return true;

    Optional<User> optionalUser = userRepository.findById(userId);

    // Check if the user exists before checking it against the authentication object, if not user exists returns false,
    return optionalUser.filter(user -> authentication.getName().equals(user.getUsername())).isPresent();
  }

  @Override
  public boolean checkRestaurantOwner(Authentication authentication, Long id) {
    if (containsRole(authentication, Role.ROLE_ADMIN.toString()))
      return true;

    if (!containsRole(authentication, Role.ROLE_OWNER.toString()))
      return false;

    Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

    if (optionalRestaurant.isEmpty())
      return false;

    User owner = optionalRestaurant.get().getOwner();
    return owner != null && authentication.getName().equals(owner.getEmail());
  }

  @Override
  public boolean checkDishOwner(Authentication authentication, Long dishId) {
    if (containsRole(authentication, Role.ROLE_ADMIN.toString()))
      return true;

    if (!containsRole(authentication, Role.ROLE_OWNER.toString()))
      return false;

    Optional<Dish> optionalDish = dishRepository.findById(dishId);

    if (optionalDish.isEmpty())
      return false;
    
    User owner = optionalDish.get().getRestaurant().getOwner();
    return owner != null && authentication.getName().equals(owner.getEmail());
  }

  /**
   * Checks if the authentication object contains the role given.
   *
   * @param authentication the authentication object. Should be given by the framework.
   * @param role           the role to check for.
   * @return returns {@code true} if the authentication contains the role.
   */
  private boolean containsRole(Authentication authentication, String role) {
    return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
  }

}
