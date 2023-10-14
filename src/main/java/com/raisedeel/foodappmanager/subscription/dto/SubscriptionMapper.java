package com.raisedeel.foodappmanager.subscription.dto;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.subscription.model.Subscription;
import com.raisedeel.foodappmanager.user.model.User;
import org.mapstruct.*;

/**
 * A Mapper interface annotated with {@link Mapper} that facilitates the conversion and updating between
 * {@link Subscription} and {@link SubscriptionDto} objects using MapStruct. <br/>
 * This interface defines methods for mapping Subscription objects to SubscriptionDto objects and vice versa.
 * It also provides a method for updating a Subscription object from a SubscriptionDto while ignoring null values.
 *
 * @see Mapper
 * @see Subscription
 * @see SubscriptionDto
 */
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SubscriptionMapper {
  /**
   * Maps a {@link Subscription} to a {@link SubscriptionDto}, mapping the {@link User} and {@link Restaurant} objects
   * to their corresponding IDs.
   *
   * @param subscription the {@link Subscription} object to be mapped.
   * @return the resulting {@link SubscriptionDto} containing user and restaurant IDs.
   */
  @Mapping(target = "userId", source = "user")
  @Mapping(target = "restaurantId", source = "restaurant")
  SubscriptionDto subscriptionToDto(Subscription subscription);

  /**
   * Updates a {@link Subscription} object from a {@link SubscriptionDto}, while ignoring null values.
   * This method is useful for selectively updating Subscription attributes.
   *
   * @param subscriptionDto the {@link SubscriptionDto} containing updated information.
   * @param subscription    the {@link Subscription} object to be updated.
   * @return the updated {@link Subscription} object.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Subscription updateSubscriptionFromDto(SubscriptionDto subscriptionDto, @MappingTarget Subscription subscription);

  /**
   * Retrieves the user's ID from a {@link User} object.
   * This is a helper method used during mapping.
   *
   * @param user the {@link User} object from which to extract the ID.
   * @return the ID of the user.
   */
  default Long getUserId(User user) {
    return user.getId();
  }

  /**
   * Retrieves the restaurant's ID from a {@link Restaurant} object.
   * This is a helper method used during mapping.
   *
   * @param restaurant the {@link Restaurant} object from which to extract the ID.
   * @return the ID of the restaurant.
   */
  default Long getRestaurantId(Restaurant restaurant) {
    return restaurant.getId();
  }
}
