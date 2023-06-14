package com.raisedeel.foodappmanager.security.validators;

import com.raisedeel.foodappmanager.dish.model.Dish;
import com.raisedeel.foodappmanager.dish.repository.DishRepository;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationValidatorImpl implements AuthenticationValidator {

  private UserRepository userRepository;
  private RestaurantRepository restaurantRepository;
  private DishRepository dishRepository;

  @Override
  public boolean checkUserId(Authentication authentication, Map<String, String> attributes) {
    // Allow access to the admin
    if (containsRole(authentication, "ADMIN")) {
      return true;
    }

    Long id = Long.parseLong(attributes.get("id"));
    Optional<User> optionalUser = userRepository.findById(id);

    // Check if the user exists before checking it against the authentication object, if not user exists returns false,
    return optionalUser.filter(user -> authentication.getName().equals(user.getUsername())).isPresent();
  }

  @Override
  public boolean checkRestaurantOwner(Authentication authentication, Map<String, String> attributes) {
    if (containsRole(authentication, "ADMIN")) {
      return true;
    }

    if (!containsRole(authentication, "OWNER")) {
      return false;
    }

    Long id = Long.parseLong(attributes.get("id"));
    Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

    if (optionalRestaurant.isEmpty()) {
      return false;
    }

    User owner = optionalRestaurant.get().getOwner();
    return owner != null && authentication.getName().equals(owner.getEmail());
  }

  @Override
  public boolean checkDishOwner(Authentication authentication, Map<String, String> attributes) {
    if (containsRole(authentication, "ADMIN")) {
      return true;
    }

    if (!containsRole(authentication, "OWNER")) {
      return false;
    }

    Long id = Long.parseLong(attributes.get("id"));
    Optional<Dish> optionalDish = dishRepository.findById(id);

    if (optionalDish.isEmpty()) {
      return false;
    }

    User owner = optionalDish.get().getRestaurant().getOwner();
    return owner != null && authentication.getName().equals(owner.getEmail());
  }

  private boolean containsRole(Authentication authentication, String role) {
    return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role));
  }

}
