package com.raisedeel.foodappmanager.restaurant.dto;

import com.raisedeel.foodappmanager.dish.dto.DishMapper;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionMapper;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {DishMapper.class, SubscriptionMapper.class}
)
public interface RestaurantMapper {

  RestaurantDto restaurantToDto(Restaurant restaurant);

  Restaurant dtoToRestaurant(RestaurantDto restaurantDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Restaurant updateRestaurantFromDto(RestaurantDto restaurantDto, @MappingTarget Restaurant restaurant);

}
