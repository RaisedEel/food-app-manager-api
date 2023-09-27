package com.raisedeel.foodappmanager.subscription.dto;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.subscription.model.Subscription;
import com.raisedeel.foodappmanager.user.model.User;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SubscriptionMapper {
  @Mapping(target = "userId", source = "user")
  @Mapping(target = "restaurantId", source = "restaurant")
  SubscriptionDto subscriptionToDto(Subscription subscription);
  
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Subscription updateSubscriptionFromDto(SubscriptionDto subscriptionDto, @MappingTarget Subscription subscription);

  default Long getUserId(User user) {
    return user.getId();
  }

  default Long getRestaurantId(Restaurant restaurant) {
    return restaurant.getId();
  }
}
